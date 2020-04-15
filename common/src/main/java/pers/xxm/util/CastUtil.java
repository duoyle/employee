package pers.xxm.util;

import java.lang.reflect.Array;
import java.util.function.Function;

/**
 * Created by XuXuemin on 20/2/28
 */
public class CastUtil {
    // 为了消除警告泛型未知赋给泛型已知情况，例如List赋给List<Department>，此时T就是List<Department>类型
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }

    /**
     * 根据数组类型和传递的转换函数，转换成目标数组
     *
     * @param value         用|分割的值
     * @param parseFunction 转换方式
     * @param <R>           目标数组类型
     * @return 目标数组
     */
    public static <R> R[] parseArray(String value, Function<String, R> parseFunction) {
        if (CheckUtil.isNotEmpty(value) && parseFunction != null) {
            String[] values = value.split("\\|");
            return parseArray(values, parseFunction);
        }
        return null;
    }

    /**
     * 根据数组类型和传递的转换函数，转换成目标数组
     *
     * @param values        用|分割的值
     * @param parseFunction 转换方式
     * @param <R>           目标数组类型
     * @return 目标数组
     */
    public static <R> R[] parseArray(String[] values, Function<String, R> parseFunction) {
        // 这里可以做一个判断，数组元素只有一个，且该元素值中包含|分隔符则采取分割方式，此想法取消。
        if (!CheckUtil.isNotEmpty(values) || parseFunction == null) {
            return null;
        }
        // 否则按照传递的是数组处理
        // 为了得到泛型R的Class对象，只有Class对象才能创建实例或数组实例
        R first = parseFunction.apply(values[0]);
        R[] result = CastUtil.cast(Array.newInstance(first.getClass(), values.length));
        result[0] = first;
        for (int i = 1; i < values.length; i++) {
            result[i] = parseFunction.apply(values[i]);
        }
        return result;
    }

    /**
     * 字符串转换成整数数组
     *
     * @param value 用|分割的值
     * @return 整数数组
     */
    public static int[] parseIntArray(String value) {
        if (CheckUtil.isNotEmpty(value)) {
            String[] values = value.split("\\|");
            return parseIntArray(values);
        }
        return null;
    }

    /**
     * 字符串转换成整数数组
     *
     * @param values 用|分割的值
     * @return 整数数组
     */
    public static int[] parseIntArray(String[] values) {
        if (!CheckUtil.isNotEmpty(values)) {
            return null;
        }
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Integer.parseInt(values[i]);
        }
        return result;
    }

    /**
     * 字符串转换成长整数数组
     *
     * @param value 用|分割的值
     * @return 长整数数组
     */
    public static long[] parseLongArray(String value) {
        if (CheckUtil.isNotEmpty(value)) {
            String[] values = value.split("\\|");
            return parseLongArray(values);
        }
        return null;
    }

    /**
     * 字符串转换成长整数数组
     *
     * @param values 用|分割的值
     * @return 长整数数组
     */
    public static long[] parseLongArray(String[] values) {
        if (!CheckUtil.isNotEmpty(values)) {
            return null;
        }
        long[] result = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Long.parseLong(values[i]);
        }
        return result;
    }

    /**
     * 字符串转换成小数数组
     *
     * @param value 用|分割的值
     * @return 小数数组
     */
    public static double[] parseDoubleArray(String value) {
        if (CheckUtil.isNotEmpty(value)) {
            String[] values = value.split("\\|");
            return parseDoubleArray(values);
        }
        return null;
    }

    /**
     * 字符串转换成小数数组
     *
     * @param values 用|分割的值
     * @return 小数数组
     */
    public static double[] parseDoubleArray(String[] values) {
        if (!CheckUtil.isNotEmpty(values)) {
            return null;
        }
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Double.parseDouble(values[i]);
        }
        return result;
    }
}
