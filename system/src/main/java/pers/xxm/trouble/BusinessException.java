package pers.xxm.trouble;

import lombok.Getter;
import org.apache.http.HttpStatus;
import pers.xxm.resource.ResourceManager;

/**
 * Created by XuXuemin on 20/3/1
 * 用于全局统一处理的异常实例
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    private Throwable target;

    @Getter
    private int code;

    /**
     * 构建异常，使用500错误码，服务器内部错误
     */
    public BusinessException() {
        this(HttpStatus.SC_INTERNAL_SERVER_ERROR, ResourceManager.getMessage("internal.server.error"));
    }

    /**
     * 构建异常，使用500错误码，服务器内部错误
     */
    public BusinessException(Throwable target) {
        this(HttpStatus.SC_INTERNAL_SERVER_ERROR, ResourceManager.getMessage("internal.server.error"), target);
    }

    /**
     * 构建业务异常对象
     *
     * @param code    错误状态码
     * @param message 异常信息
     */
    public BusinessException(int code, String message) {
        super(message, null, true, false);
        this.code = code;
        this.target = this; // 业务异常目标异常是自身
    }

    /**
     * 将系统异常构建成业务异常
     * @param code 业务异常状态码
     * @param message 异常信息
     * @param target 真实异常
     */
    public BusinessException(int code, String message, Throwable target) {
        super(message, null, true, false);
        this.code = code;
        this.target = target;
    }

    /**
     * 递归找到目标（诱因）异常
     * @param throwable 要查找的异常
     * @return 目标异常
     */
    public static Throwable getTargetException(Throwable throwable) {
        Throwable causeThrowable = throwable.getCause();
        if (causeThrowable == null) {
            return throwable;
        }
        return getTargetException(causeThrowable);
    }

    /**
     * 包装成业务异常
     * @param throwable 被包装的异常对象
     * @return 业务异常对象
     */
    public static BusinessException wrapException(Throwable throwable) {
        Throwable target = getTargetException(throwable); // 取得target异常，真正的诱因异常
        if (target instanceof BusinessException) {
            return (BusinessException) target;
        }
        // 将异常播包裹成BusinessException
        if (target instanceof ReflectiveOperationException) {
            return new BusinessException(HttpStatus.SC_NOT_FOUND, ResourceManager.getMessage("not.found"), target);
        } else if (target instanceof IllegalArgumentException) {
            return new BusinessException(HttpStatus.SC_BAD_REQUEST, ResourceManager.getMessage("bad.request"), target);
        } else {
            return new BusinessException(target);
        }
    }

}
