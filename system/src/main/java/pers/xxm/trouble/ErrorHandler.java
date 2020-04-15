package pers.xxm.trouble;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by XuXuemin on 20/3/16
 * 这个类作为用返回结果信息（包含状态码和数据）代替抛异常，这种方式不够优雅。
 */
@Deprecated
public class ErrorHandler {

    public static void exceptionHandle(Exception exception, HttpServletRequest request,
                                       HttpServletResponse response) {
    }

    private static void failureHandle(Object message, HttpServletRequest request,
                                      HttpServletResponse response) {
    }

}
