package pers.xxm.empl.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XuXuemin on 20/3/3
 * 数据层和业务层的工厂的基类，为了实现减少new太多对象
 */
public abstract class Factory {
    // 具体的类可以再配置文件中配置好，然后读取进来类名，反射创建，Spring似乎如此做的。
    // 如果有线程安全可使用ConcurrentHashMap，但感觉不用，首次才会出现同步，出现问题也是替代原来的。
    protected Map<String, Object> objectMap = new HashMap<>(); // 保存业务层和数据层创建的实例

    /**
     * 获取对应类的实例，用于业务层和数据层实例
     * @param cls 要取得的实例的类型
     * @param <T> 要取得的实例的类
     * @return 实例
     * @throws InstantiationException 实例化异常（构造不存在）
     * @throws IllegalAccessException 访问权限异常（例如private成员）
     */
    public abstract <T> T getObject(Class<T> cls) throws InstantiationException, IllegalAccessException;
}
