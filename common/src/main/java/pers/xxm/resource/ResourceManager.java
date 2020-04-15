package pers.xxm.resource;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Created by XuXuemin on 20/3/6
 */
public class ResourceManager {
    // 每次ResourceBundle调用getBundle时会从缓存中读取，也就是ResourceBundle做了缓存，但其缓存机制比较繁琐提供了动态更新接口
    // 这里再次做了缓存，其实是不应该的，直接用getBundle就是从缓存中取的。这里使用自己缓存的资源实例包。

    // 资源文件应该在CLASSPATH下，放在src/main下的内容会加载到WEB-INF（其下自动加到CLASSPATH）下吧。

    // ConcurrentHashMap线程安全，读取并发，写同步但支持部分并发，实现较复杂（JDK1.8加入红黑树和CAS算法）
    private static final Map<String, ResourceManager> resourceManagers = new ConcurrentHashMap<>();

    private static final String PAGE_NAME = "page"; // 文件名必须叫page.properties，下同
    private static final String MESSAGE_NAME = "message";

    /**
     * 添加一个ResourceManager对象（对应一个ResourceBundle）
     *
     * @param name 标示名称
     * @param file 资源文件名称（在默认resources路径下不用含路径）
     */
    public static ResourceManager load(String name, String file) {
        ResourceBundle bundle = ResourceBundle.getBundle(file);
        ResourceManager manager = new ResourceManager(bundle);
        ResourceManager.resourceManagers.put(name, manager);
        return manager;
    }

    /**
     * 添加一个ResourceManager对象（对应一个ResourceBundle）
     *
     * @param name 资源文件名称，在默认resources路径下，必须是.properties文件且不能包含后缀
     */
    public static ResourceManager load(String name) {
        ResourceBundle bundle = ResourceBundle.getBundle(name);
        ResourceManager manager = new ResourceManager(bundle);
        ResourceManager.resourceManagers.put(name, manager);
        return manager;
    }

    /**
     * 取得对应名称的ResourceManager
     *
     * @param name 要取得ResourceManager的名称（ID或键）
     * @return 对应名称的ResourceManager
     */
    public static ResourceManager get(String name) {
        // HashMap中get不存在的key不会抛异常，直接返回null，ConcurrentHashMap则会抛异常，NullPointerException
        if (ResourceManager.resourceManagers.containsKey(name)) {
            return ResourceManager.resourceManagers.get(name);
        }
        return null;
    }

    /**
     * 获取对应key的页面路径（URI）
     *
     * @param key  资源文件中Key值
     * @param args 格式化参数
     * @return 资源文件中Value值
     */
    public static String getPage(String key, Object... args) {
        ResourceManager rsrc = ResourceManager.get(PAGE_NAME);
        if (rsrc == null) {
            rsrc = ResourceManager.load(PAGE_NAME);
        }
        return rsrc.getString(key, args);
    }

    /**
     * 获取提示信息内容
     *
     * @param key  资源文件中的Key值
     * @param args 传递的参数
     * @return 资源文件中传递参数后的Value值
     */
    public static String getMessage(String key, Object... args) {
        ResourceManager rsrc = ResourceManager.get(MESSAGE_NAME);
        if (rsrc == null) {
            rsrc = ResourceManager.load(MESSAGE_NAME);
        }
        return rsrc.getString(key, args);
    }

    /*
    非静态部分
     */
    private ResourceBundle resourceBundle;

    // 禁止外部实例化
    private ResourceManager(ResourceBundle bundle) {
        this.resourceBundle = bundle;
    }

    /**
     * 取得对应Key的值
     *
     * @param key  要取得值的Key
     * @param args 格式化所需参数
     * @return 对应Key的值
     */
    public String getString(String key, Object... args) {
        // 这里如果为String...则传递该参数时有一个警告，防止参数误判，也就是可变参数建议类型完全匹配
        String value = new String(this.resourceBundle.getString(key).getBytes(
                StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8); // 读取支持中文的内容
        if (args == null || args.length == 0) {
            return value;
        }
        return MessageFormat.format(value, args);
    }

}
