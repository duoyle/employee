package pers.xxm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XuXuemin on 20/2/28
 */
public class CheckUtil {
    /**
     * 判断是否为数字
     * @param content 要判断的字符串内容
     * @return 全是数字返回true，否则false
     */
    public static boolean isNumber(String content) {
        if (content == null) {
            return false;
        }
        for (int i = 0; i < content.length(); i++) {
            if (!Character.isDigit(content.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为小数
     * @param content 要判断的字符串内容
     * @return 是小数返回true，否则false
     */
    public static boolean isDecimal(String content) {
        if (!CheckUtil.isNotEmpty(content)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[-+]?(\\d+(\\.\\d*)?|\\d*\\.\\d+)");
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }

    /**
     * 判断是否为空(null或"")
     * @param content 要判断的字符串内容
     * @return 非空返回true，否则false
     */
    public static boolean isNotEmpty(String content) {
        return (content != null && content.length() != 0);
    }

    /**
     * 判断是否为空(null或空数组)
     * @param contents 要判断的字符串内容
     * @return 非空返回true，否则false
     */
    public static boolean isNotEmpty(Object[] contents) {
        return (contents != null && contents.length > 0);
    }

    /**
     * 判断是否为日期格式(YYYY-MM-DD或YYYY-MM-DD HH:MM:SS)
     * @param content 要判断的字符串内容
     * @return 是日期返回true，否则false
     */
    public static boolean isDate(String content) {
        if (!CheckUtil.isNotEmpty(content)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher matcher = pattern.matcher(content);
        if (matcher.matches()) {
            return true;
        }
        pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
        matcher = pattern.matcher(content);
        return matcher.matches();
    }

}
