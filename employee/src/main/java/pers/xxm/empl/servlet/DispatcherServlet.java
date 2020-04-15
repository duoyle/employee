package pers.xxm.empl.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpStatus;
import pers.xxm.bean.BeanFactory;
import pers.xxm.bean.Validation;
import pers.xxm.empl.common.Constant;
import pers.xxm.empl.service.IService;
import pers.xxm.empl.vo.Page;
import pers.xxm.resource.ResourceManager;
import pers.xxm.trouble.BusinessException;
import pers.xxm.trouble.ErrorCode;
import pers.xxm.trouble.ExceptionHandler;
import pers.xxm.util.CastUtil;
import pers.xxm.util.DateUtil;
import pers.xxm.util.GsonUtil;
import pers.xxm.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XuXuemin on 20/3/2
 */
public abstract class DispatcherServlet extends HttpServlet {
    /* url-pattern：
     1. 通配符只能用一次，且不能出现在中间，/page/*.do不合法，/page/*和*.do合法。经debug测试，*.do匹配不进入该Servlet中（？）。
     2. 优先精确和最长匹配。/*匹配/page、/page/list.do等。/匹配/page/dept，不匹配/page/list.do。*.jsp匹配所有路径下的jsp文件。
     3. 为/page/list.jsp，servletPath是/page/list.jsp，pathInfo是null，为/page/*，servletPath是/page，pathInfo是/list.jsp。
     */

    /*
    1. 前端提交数据可以为json字符串，如：ajax里data:JSON.stringify(json_object)或$.post(url,{paramName: <json_str>}, func)。
    2. 对应上面的ajax读取方式可以是BufferedReader，上面post读取用getParameter("paramName")，然后解析字符串。
     */
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Map<String, String> searchColumns;

    /**
     * 初始化，进行自愿读取，自动调用
     */
    @Override
    public void init() {
        // 该方法不需要再执行父类的，父类自动调用带配置参数的init，然后调用无参的init（覆写自动调用子类的）
    }

    /**
     * 服务入口函数，自动调用，doGet和doPost是由该方法调用的
     */
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            // 封装表单处理，封装后按二进制流读取的，原则上只有Post才有表单，这里也可参照父类service方法区分Get和Post
            this.request = ServletFileUpload.isMultipartContent(req) ? new FileUploadWrapper(req) : req;
            this.response = resp;
            // 调用具体行为方法，request.getMethod用来区分GET和POST等
            String methodName = this.getMethodName();
            Method method = this.getClass().getDeclaredMethod(methodName);
            Object toPath = method.invoke(this); // 调用子类方法名

            // 一般使用绝对路径（在ContextPath下），以/<项目名>开头。requestURI=contextPath+servletPath+pathInfo。
            // 这里若不进行跳转或写入前端的请求返回结果将是空白。
            if (toPath != null) {
                // jsp里pageContext对象可以访问其他三个域对象，所以其中的forward本质可能也如此调用。跳转同时传递参数
                // resp.sendRedirect(toPath.toString()); // 返回给客户端后客户端再次访问新地址，浏览器地址变化
                req.getRequestDispatcher(toPath.toString()).forward(this.request, this.response); // 地址栏不变
            }
        } catch (BusinessException bizException) {
            ExceptionHandler.exceptionHandle(bizException, req, resp);
        } catch (Exception exception) {
            ExceptionHandler.exceptionHandle(exception, req, resp);
        }
    }

    /**
     * 根据规则验证参数是否合法
     *
     * @param bean              要验证的对象
     * @param necessaryParams   必须的参数
     * @param dispensableParams 可省的参数
     * @return true为合法，否则false
     */
    protected boolean validate(Object bean, String necessaryParams, String dispensableParams) {
        Validation validation = new Validation(necessaryParams, dispensableParams, this.request);
        return this.validate0(bean, validation);
    }

    /**
     * 根据规则验证参数是否合法
     *
     * @param bean            要验证的对象
     * @param necessaryParams 必须的参数
     * @return true为合法，否则false
     */
    protected boolean validate(Object bean, String necessaryParams) {
        Validation validation = new Validation(necessaryParams, this.request);
        return this.validate0(bean, validation);
    }

    /**
     * 通过反射动态设置Servlet子类的属性
     * 如果还需要设置其他属性（不能动态设置的），子类覆写该方法，并调用该父类方法（建议）
     *
     * @param bean 要验证的对象
     */
    protected void setBean(Object bean) {
        // 反射动态设置属性
        BeanFactory factory = new BeanFactory(this.request);
        try {
            factory.setObject(bean);
        } catch (Exception ex) {
            throw BusinessException.wrapException(ex);
        }
    }


    /**
     * 验证并设置参数到对象（反射机制），使用子类提供的验证列进行验证
     *
     * @param bean 要设置的对象，经过初始化后的
     */
    protected void validSetBean(Object bean) {
        if (this.validate(bean, this.getValidateParams())) {
            this.setBean(bean);
        }
    }

    /**
     * 验证并设置参数到对象（反射机制）
     *
     * @param bean      要设置的对象，经过初始化后的
     * @param necessary 要验证的字符串内容
     */
    protected void validSetBean(Object bean, String necessary) {
        if (this.validate(bean, necessary)) {
            this.setBean(bean);
        }
    }

    /**
     * 验证并设置参数到对象（反射机制）
     *
     * @param bean        要设置的对象，经过初始化后的
     * @param necessary   要验证的字符串内容
     * @param dispensable 要验证的可选参数（参数可无，若有验证合法性）
     */
    protected void validSetBean(Object bean, String necessary, String dispensable) {
        if (this.validate(bean, necessary, dispensable)) {
            this.setBean(bean);
        }
    }

    /**
     * 取得对应参数名的值，按类型转换
     *
     * @param name 参数名
     * @param type 类型
     * @param <T>  结果值类型
     * @return 参数值
     */
    protected <T> T getParamValue(String name, Class<T> type) {
        if (StringUtil.isEmpty(name) || type == null) {
            return null;
        }
        try {
            String simpleName = type.getSimpleName();
            if (simpleName.endsWith("[]")) {
                String[] values = this.request.getParameterValues(name);
                switch (simpleName) {
                    case "String[]": // case中无法定义和上面case相同名字的变量，可使用{}括起来去定义
                        return CastUtil.cast(CastUtil.parseArray(values, x -> x));
                    case "Integer[]":
                        return CastUtil.cast(CastUtil.parseArray(values, Integer::parseInt));
                    case "Long[]":
                        return CastUtil.cast(CastUtil.parseArray(values, Long::parseLong));
                    case "Double[]":
                        return CastUtil.cast(CastUtil.parseArray(values, Double::parseDouble));
                    case "int[]":
                        return CastUtil.cast(CastUtil.parseIntArray(values));
                    case "long[]":
                        return CastUtil.cast(CastUtil.parseLongArray(values));
                    case "double[]":
                        return CastUtil.cast(CastUtil.parseDoubleArray(values));
                }
            } else {
                String value = this.request.getParameter(name);
                switch (simpleName) {
                    case "String":
                        return CastUtil.cast(value);
                    case "Integer":
                    case "int":
                        return CastUtil.cast(Integer.parseInt(value));
                    case "Long":
                    case "long":
                        return CastUtil.cast(Long.parseLong(value));
                    case "Double":
                    case "double":
                        return CastUtil.cast(Double.parseDouble(value));
                    case "Date":
                        if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            return CastUtil.cast(DateUtil.parseDate(value));
                        } else if (value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                            return CastUtil.cast(DateUtil.parseTime(value));
                        }
                }
            }
            return null;
        } catch (ParseException ex) {
            throw new BusinessException(ErrorCode.PARAM_ILLEGAL, ResourceManager.getMessage("param.illegal", name));
        }
    }

    /**
     * 取得请求路径，在路径末尾添加后缀.jsp
     * @return 返回存在的文件路径
     */
    protected String getForwardPath() {
        // 如果jsp文件，存在html静态页文件，则最好放入其他路径在EscapeServlet跳转，不属于普通servlet控制范畴
        // 供子类调用，子类如果不采用这种方式，不必调用该方法
        String toPath = getValidPath().substring(Constant.SERVLET_PATH.length());
        // 从Servlet路径中获取真实路径
        toPath = Constant.PAGE_PATH + toPath + Constant.PAGE_SUFFIX;
        // 判断文件是否存在
        if (!existsFile(toPath)) {
            throwNotFoundException();
        }
        return toPath;
    }

    /**
     * 判断访问路径对应的文件是否存在
     * @param path ServletPath + PathInfo
     * @return 存在为true，否则false
     */
    protected boolean existsFile(String path) {
        // 判断文件是否存在
        String filePath = this.getServletContext().getRealPath(path);
        return new File(filePath).exists();
    }

    /**
     * 上传文件，子类调用
     */
    protected void upload() {
        FileUploadWrapper fileUpload = (FileUploadWrapper) this.request;
        List<FileItem> fileItems = fileUpload.getFileItems();
        if (fileItems == null || fileItems.size() == 0) {
            return;
        }
        try {
            for (FileItem fileItem : fileItems) {
                String savePath = this.getFilePath();
                File file = new File(savePath, this.getFileName(fileItem));
                fileItem.write(file); // 内部创建对应File的OutputStream，然后将数据写入
                fileItem.delete(); // 删除临时文件
            }
        } catch (Exception ex) {
            throw BusinessException.wrapException(ex);
        }
    }

    /**
     * 取得根路径（结尾不带/）
     *
     * @return 根路径
     */
    public String getBasePath() {
        return this.request.getScheme() + "://" + this.request.getServerName() + ":"
                + this.request.getServerPort() + "/" + this.request.getContextPath();
    }

    /**
     * 取得查询所使用的列
     *
     * @return 列名
     */
    protected Map<String, String> getSearchColumns() {
        return this.searchColumns;
    }

    /**
     * 将计算结果写入回应流中
     *
     * @param result 要写入的数据
     * @throws IOException IO异常
     */
    protected void writeResponse(Object result) throws IOException {
        // 跳转到jsp或html页面本质也是这种写入应答方式
        this.response.setContentType("application/json");
        PrintWriter writer = this.response.getWriter();
        // 使用Jackson转json，会自动读get方法。接收端以json接收这里必须转json，否则浏览器会以error接收
        writer.write(GsonUtil.getGson().toJson(result));
    }

    /**
     * 取得路径中最后斜杠后的，同时去掉指定末尾字符串后的内容
     *
     * @param tail 要移除的结尾标识，实际移除内容会在tail前加点（.）形成新的tail再去移除
     */
    protected String getPathFlag(String tail) {
        String path = getValidPath();
        Pattern pattern = Pattern.compile(String.format("(?<=/)\\w+(?=\\.%s$)", tail));
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * 引发未发现访问目标异常
     */
    protected void throwNotFoundException() {
        throw new BusinessException(HttpStatus.SC_NOT_FOUND, ResourceManager.getMessage("not.found"));
    }

    /* ********************** 子类根据需要覆写的方法 ********************** */

    /**
     * 生成文件名，子类可覆写，否则自动生成文件名
     *
     * @param item 要上传的文件对象
     * @return 要保存的文件名
     */
    protected String getFileName(FileItem item) {
        return UUID.randomUUID().toString().concat(".").concat(item.getName());
    }

    /**
     * 要保存的文件路径，若有文件上传建议子类覆写，否则保存到项目的upload路径下
     *
     * @return 保存文件路径
     */
    protected String getFilePath() {
        return this.getServletContext().getRealPath("/upload"); // 默认上传路径在项目目录下的upload下
    }

    /**
     * 是否需要上传，根据上传的表单类型判断，子类一般不用覆写
     *
     * @return 是否需要上传
     */
    protected boolean isUpload() {
        return this.request instanceof FileUploadWrapper; // 判断是否是文件上传
    }

    /**
     * 请求地址处理和定位，建议子类覆写该方法，否则直接跳转到请求路径
     * 根据需求处理和过滤访问的非.do结尾的地址
     *
     * @return 访问（跳转）地址
     */
    protected String locate() throws Exception {
        return this.request.getServletPath() + this.request.getPathInfo();
    }

    /**
     * 读取分页数据
     * @param service 服务层对象
     * @param page 初始化后的分页数据对象
     * @param <K> 结果的Key类型
     * @param <V> 结果的Value类型
     * @throws SQLException SQL异常
     * @throws IOException IO异常
     */
    protected <K, V> void listInPage(IService<K, V> service, Page<V> page) throws SQLException, IOException {
        // pageSize和currentPage是可选参数
        if (this.validate(page, "currentPage|pageSize")) {
            this.setBean(page);
            service.listInPage(page); // 读取分页数据到page对象中
            this.writeResponse(page); // 写入到回应数据
        }
    }

    /**
     * 取得访问地址的方法名
     * @return 方法名
     */
    protected String getMethodName() {
        // 子类一般使用xxx/*方式匹配的使用该方式，如果子类不是该规则需要覆写该方法
        String pathFlag = this.request.getPathInfo();
        if (pathFlag == null) {
            throwNotFoundException();
        } else if (pathFlag.lastIndexOf("/") == 0 && pathFlag.endsWith(".do")) {
            // URLPatterns中的*匹配到/insert.do这种格式才算成功
            return pathFlag.substring(1, pathFlag.length() - 3);
        }
        return "locate"; // 非.do结尾的地址调用该方法
    }

    /* ********************** 抽象方法 ********************** */

    /**
     * 获取当前Servlet的标题（名称，例如部门，员工），用于填充提示信息中的参数
     *
     * @return 标题名称
     */
    protected abstract String getTitle();

    /**
     * 默认验证的参数（列名，多个中间用|分割）
     *
     * @return 验证参数
     */
    protected abstract String getValidateParams();

    /* ********************* 私有方法 *********************** */

    // 读取上一个页面的地址
    private String getRefererPath() {
        String referer = this.request.getHeader("referer");
        if (referer == null) {
            referer = this.request.getRequestURL().toString();
        }
        return referer.replace(this.getBasePath(), "");
    }

    // 根据验证对象去验证，内部调用
    private boolean validate0(Object bean, Validation validation) {
        try {
            if (validation.validate(bean)) {
                return true;
            }
            throw new BusinessException(ErrorCode.VALIDATE_FAILURE, validation.getMessage());
        } catch (Exception ex) {
            throw BusinessException.wrapException(ex);
        }
    }

    // 取得访问路径，不包含ContextPath，后台跳转不需要
    private String getValidPath() {
        String path = this.request.getPathInfo();
        return path == null ? this.request.getServletPath() : this.request.getServletPath() + path;
    }

}
