package pers.xxm.trouble;

/**
 * Created by XuXuemin on 20/3/2
 */
public class ErrorCode {

    public static final int UNKNOWN = 5000; // 服务器发生了未知错误
    public static final int PARAM_ILLEGAL = 5001; // 参数不合法
    public static final int VALIDATE_FAILURE = 5002; // 验证失败，数据来自message中validate.xxx
    public static final int EXISTS = 5003; // 数据已存在
    public static final int NOT_EXISTS = 5004; // 该数据不存在，可能已被修改

}
