package pers.xxm.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by XuXuemin on 18/11/6
 */
public class StringUtil {

    /*
    加号会被优化成StringBuilder的append，例如：
    String str = "a" + "bc" 优化为 new StringBuilder().append("a").append("bc")
    所以for循环中使用加号+被优化后会new很多次StringBuilder，效率降低，此时应该手动使用StringBuilder。
    如果单纯连接两个字符串，可使用concat，比StringBuilder（很多次连接更高效）更高效。
     */

    /**
     * create by xuxm 2018/09/18
     * 字符串格式化指定长度，不够的左边补0，大于指定长度保留原内容
     * @param src 要处理的字符串
     * @param len 要格式化的长度
     * @return 格式化后的结果
     */
    public static String leftPad(String src, int len) {
        return StringUtils.leftPad(src, len, "0");
    }

    /**
     * create by xuxm 2018/09/18
     * 数字格式化指定长度，不够的左边补0，大于指定长度保留原内容，转为字符串返回
     * @param src 要处理的数字
     * @param len 要格式化的长度
     * @return 格式化后的结果
     */
    public static String leftPad(int src, int len) {
        return StringUtils.leftPad(String.valueOf(src), len, "0");
    }

    /**
     * 把数字连接成字符串，空格分割
     *
     * @param numbers 要连接的数字
     * @return 连接成的字符串
     */
    public static String joinNumber(int... numbers) {
        if (numbers == null || numbers.length == 0) {
            return "";
        }
        StringBuilder value = new StringBuilder();
        for (int num : numbers) {
            if (value.length() == 0) {
                value.append(num);
            } else {
                value.append(" ").append(num);
            }
        }
        return value.toString();
    }

    /**
     * 字符串首字母大写
     * @param value 要首字母大写的字符串
     * @return 首字母大写后的字符串
     */
    public static String initCap(String value) {
        if (value == null ) {
            return value;
        }
        String first = value.substring(0, 1);
        return first.toUpperCase().concat(value.substring(1));
    }

    /**
     * 判断字符串是否为空
     * @param value 要判断的字符串
     * @return true表示空，否则false
     */
    public static boolean isEmpty(CharSequence value) {
        return StringUtils.isEmpty(value);
    }

}
