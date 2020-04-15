package pers.xxm.resource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pers.xxm.util.GsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XuXuemin on 20/3/22
 */
public class Comments {
    private static Map<String, String> titles;

    static {
        try {
            Gson gson = GsonUtil.getGson();

            // class.getResourceAsStream参数若是绝对路径（带/）则表示CLASSPATH下的，相对路径是当前类所在路径。
            // getClassLoader.getResourceAsStream参数只能是相对路径，就是指CLASSPATH下的。
            InputStream inputStream = Comments.class.getClassLoader().getResourceAsStream("comments.json");
            Reader reader = new InputStreamReader(inputStream);
            // 传递Class无法传递其中的泛型吧，下面是Google的解决方式
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            titles = gson.fromJson(reader, type);
            reader.close();
        } catch (IOException ex) {
            titles = new HashMap<>();
        }
    }

    public static String get(String name) {
        if (titles == null) {
            return "";
        }
        return titles.get(name);
    }
}
