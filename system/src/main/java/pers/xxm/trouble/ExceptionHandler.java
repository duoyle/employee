package pers.xxm.trouble;

import lombok.Getter;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.xxm.resource.ResourceManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by XuXuemin on 20/3/14
 */
public class ExceptionHandler {
    /*
    如果一些错误消息以正确状态返回，以便于客户端（浏览器）以json接收，解析和展示。则应该统一都用正确状态返回，用错误码表示。
      此时，验证使用HashMap存储字段和消息，接收端应该知道接收的信息内容。错误消息以Object方式存储，回应时转成JSON串。

    1. slf4j的包名为org.slf4j，桥接包一般由Logger提供者提供或者slf4j提供，新版本一般厂商提供。桥接包里，会有org.slf4j包。
    2. 桥接包里org.slf4j里定义StaticLoggerBinder类来取得具体Logger的工厂实例。这部分内容虽然在桥接包中，但要都是遵照slf4j的接口。
    3. 添加了多个厂商的StaticLoggerBinder会出现警告，并随机选一个使用，所以建议只导入一个。
     */
    @Getter
    private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    // Java的日志可以使用System.out.print方式，这里的out需要编写额外的代码指定输出终端（一般System会初始化为控制台，控制台程序吗？）。
    // slf4j是一个门面模式，只提供抽象接口，具体实现还要依赖具体日志框架，如log4j、java.util.logging等。log4j可用log4j.properties配置。

    /**
     * 处理异常，根据Ajax和普通请求回应
     *
     * @param exception 异常对象
     * @param request   请求
     * @param response  回应
     */
    public static void exceptionHandle(Exception exception, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (exception == null || request == null || response == null) {
            return;
        }
        BusinessException bizException = BusinessException.wrapException(exception); // 包装成业务异常
        ExceptionHandler.log(bizException); // 打印日志
        exceptionProcess(bizException, request, response); // 回应处理
    }

    /**
     * 处理异常，根据Ajax和普通请求回应
     *
     * @param bizException 异常对象
     * @param request      请求
     * @param response     回应
     */
    public static void exceptionHandle(BusinessException bizException, HttpServletRequest request,
                                       HttpServletResponse response) throws IOException, ServletException {
        if (bizException == null || request == null || response == null) {
            return;
        }
        ExceptionHandler.log(bizException); // 打印日志
        exceptionProcess(bizException, request, response); // 回应处理
    }

    /**
     * 处理异常，根据Ajax和普通请求回应
     *
     * @param exception 异常对象
     */
    public static void exceptionHandle(Exception exception) {
        if (exception == null) {
            return;
        }
        BusinessException bizException = BusinessException.wrapException(exception);
        ExceptionHandler.log(bizException); // 打印日志
    }

    // 根据异常创建回应结果对象
    private static ErrorInfo createErrorInfo(BusinessException bizException) {
        // 构建错误信息
        ErrorInfo error = new ErrorInfo();
        error.setStatus(bizException.getCode());
        error.setMessage(bizException.getMessage());
        return error;
    }

    // 根据异常设定HTTP错误状态码，暂时不用，可处理的都用正确状态返回。这样Ajax需要在失败中接收，跳转会出错
    private static void httpStatusProcess(BusinessException ex, Consumer<Integer> consumer) {
        if (ex.getCode() < 5000) {
            consumer.accept(ex.getCode());
        } else {
            consumer.accept(500); // 500表示所有业务类型的异常和服务器内部错误
        }
    }

    private static void exceptionProcess(BusinessException ex, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        ErrorInfo error = createErrorInfo(ex);
        resp.setStatus(error.getStatus()); // 这里也可以将response传入设置，但函数式支持对状态更多的处理。
        // 如果按Ajax来设定状态，这里可提到外部接口方法中exceptionHandle和failureHandle中，然后根据具体设定装填
        if (isAjaxRequest(req) && error.getStatus() >= 5000) {
            // ContentType的编码虽然是通过response.setCharacterSet设定但没有ContentType这一项浏览器会按自己的编码进行解码。
            // 解码时，如果编码是UTF-8，则只能用UTF-8解码，否则乱码。UTF-16开头有标示（大头小头），能识别。
            // Safari和Chrome会和文本编辑器一样自动识别编码，但可能会错，例如"你好"的UTF-8码，Safari用UTF-8，Chrome用GBK则出错。
            // 如果回应Ajax异步请求不需要ContentType，因为结果不需要html展示吧。使用Writer写入默认按UTF-8取得字节流码吧。

            resp.setContentType("text/html"); // Ajax回应json数据也用text/html
            resp.getWriter().write(error.getMessage()); // 错误时以字符串方式，否则会带着引号
        } else {
            req.setAttribute("error", error);
            req.getRequestDispatcher(ResourceManager.getPage("error.page")).forward(req, resp);
        }
    }

    // 判断是否是Ajax请求
    private static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    // 打印日志
    private static void log(BusinessException bizEx) {
        logger.error(String.valueOf(bizEx.getCode()), bizEx.getTarget());
    }
}
