package pers.xxm.trouble;

import lombok.Data;

/**
 * Created by XuXuemin on 20/3/14
 */
@Data
public class ErrorInfo {
    // 客户端（浏览器）以json接收，解析和展示，此时信息应该是Object类型。
    private int status; // 状态，来自ErrorCode中的标记
    private String message; // 错误信息，可能是Map、数组、字符串等Json字符串
}
