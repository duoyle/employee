package pers.xxm.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by XuXuemin on 20/4/8
 */
public class GsonUtil {
    /**
     * 取得带有日期格式的Gson解析器
     * @return Gson解析器
     */
    public static Gson getGson() {
        return new GsonBuilder().setDateFormat(DateUtil.DATE_FORMAT).create();
    }

    /**
     * 取得带有日期时间格式的Gson解析器
     * @return Gson解析器
     */
    public static Gson getTimeGson() {
        return new GsonBuilder().setDateFormat(DateUtil.TIME_FORMAT).create();
    }
}
