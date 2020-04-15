package pers.xxm.empl.servlet;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpStatus;
import pers.xxm.resource.ResourceManager;
import pers.xxm.trouble.BusinessException;
import pers.xxm.trouble.ErrorInfo;
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
@WebServlet(name = "ErrorHandler", urlPatterns = {"/error_handler"})
public class ErrorHandler extends HttpServlet {
    /**
     * 服务入口函数，自动调用，doGet和doPost是由该方法调用的
     */
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // 这是未捕获到的异常和对应错误码的错误。ExceptionHandler处理的异常，不会跳转到这里。
        try {
            int errorCode = resp.getStatus();
            throw new BusinessException(errorCode, getMessage(errorCode));
        } catch (Exception exception) {
            ExceptionHandler.exceptionHandle(exception, req, resp);
        }
    }

    // 根据状态匹配错误信息
    private String getMessage(int status) {
        switch (status) {
            case 404:
                return ResourceManager.getMessage("not.found");
            case 403:
                return ResourceManager.getMessage("forbidden");
            case 401:
                return ResourceManager.getMessage("unauthorized");
            case 400:
                return ResourceManager.getMessage("bad.request");
            case 500:
                return ResourceManager.getMessage("internal.server.error");
            default:
                return ResourceManager.getMessage("unknown.error");
        }
    }
}
