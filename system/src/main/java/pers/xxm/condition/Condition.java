package pers.xxm.condition;

/**
 * Created by XuXuemin on 20/3/7
 */
public interface Condition {
    /**
     * 取得SQL条件语句部分，一般按照字符串格式解析
     * @return SQL条件语句
     */
    String getStatement();

    /**
     * 取得条件语句中参数值
     * @return 参数值数组
     */
    Object[] getParameters();

    /**
     * 取得条件语句中第一个参数值，适合只有一个参数的情况
     * @return 参数值
     */
    Object getParameter();

    /**
     * 将条件语句中参数和传递的参数合并成一个参数数组，参数值放在后面
     * @param params 要合并的参数
     * @return 合并后的参数数组
     */
    Object[] concatParameters(Object... params);
}
