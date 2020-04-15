package pers.xxm.empl.servlet;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import pers.xxm.empl.common.Constant;
import pers.xxm.trouble.BusinessException;
import pers.xxm.trouble.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by XuXuemin on 20/4/13
 */
@WebServlet(name = "EscapeServlet", urlPatterns = {Constant.SERVLET_PATH + "/*"})
public class EscapeServlet extends DispatcherServlet {
    /*
    1. 这里如果去掉/jsp虚拟目录前缀，则和page平级的目录权限不好控制，如果限制了，则page内还有同名目录无法访问。
    2. 1问题中如果将page，common不允许用户直接访问的放在WEB-INF里面则可以可以做到，用户访问放行就行，对部分使用Servlet转到WEB-INF下。
    3. 带了/jsp前缀，超链接等编写地址时和urlPatterns中就不匹配了，导致jsp页面中提示无法解析信息提示。
     */

    /**
     * 服务入口函数，自动调用，doGet和doPost是由该方法调用的
     */
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            // 根据urlPatterns来定义这里的路径
            String toPath = req.getPathInfo();
            toPath = Constant.PAGE_PATH + toPath + Constant.PAGE_SUFFIX;
            if (existsFile(toPath)) {
                req.getRequestDispatcher(toPath).forward(req, resp); // 地址栏不变
            } else {
                throwNotFoundException();
            }
        } catch (Exception exception) {
            ExceptionHandler.exceptionHandle(exception, req, resp);
        }
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected String getValidateParams() {
        return null;
    }
}
