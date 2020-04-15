package pers.xxm.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

/**
 * Created by XuXuemin on 18/11/6
 */
public class DateUtil {
    // 默认的日期、时间格式
    public static final String DATE_FORMAT = "yyyy-MM-dd"; // 日期格式
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; // 时间格式

    public volatile static Date maxDate; // 设置最大日期：9999-12-31
    public static Date getMaxDate() {
        if (maxDate == null) {
            synchronized (DateUtil.class) {
                if (maxDate == null) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                        maxDate = format.parse("9999-12-31");
                    } catch (ParseException ex) {
                        maxDate = new Date();
                    }
                }
            }
        }
        return maxDate;
    }

    /**
     * 将日期转为字符串
     * @param date 要转换的日期
     * @return 结果是年月日
     */
    public static String formatDate(Date date) {
        // 因为SimpleDateFormat是线程不安全的，不应用公共变量，因为每次转换都会读取和写入（修改）对象的内容
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }

    /**
     * 将日期时间转为字符串
     * @param time 日期时间
     * @return 结果是年月日时分秒
     */
    public static String formatTime(Date time) {
        DateFormat format = new SimpleDateFormat(TIME_FORMAT);
        return format.format(time);
    }

    /**
     * 将日期字符串（年月日）转为日期
     * @param value 要转换的日期字符串
     * @return 日期
     * @throws ParseException 解析异常
     */
    public static Date parseDate(String value) throws ParseException {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.parse(value);
    }

    /**
     * 将日期时间字符串（年月日时分秒）转为日期
     * @param value 要转换的日期时间字符串
     * @return 日期
     * @throws ParseException 解析异常
     */
    public static Date parseTime(String value) throws ParseException {
        DateFormat format = new SimpleDateFormat(TIME_FORMAT);
        return format.parse(value);
    }
}
