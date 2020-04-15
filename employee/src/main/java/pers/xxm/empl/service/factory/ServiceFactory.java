package pers.xxm.empl.service.factory;

import pers.xxm.empl.factory.Factory;
import pers.xxm.empl.service.proxy.ProxyFactory;
import pers.xxm.util.CastUtil;

/**
 * Created by XuXuemin on 20/3/3
 * 自动使用动态代理包装服务层对象
 */
public class ServiceFactory extends Factory {
    private volatile static Factory factory;
    public static Factory getFactory() {
        if (factory == null) {
            synchronized (ServiceFactory.class) {
                if (factory == null) {
                    factory = new ServiceFactory();
                }
            }
        }
        return factory;
    }

    @Override
    public synchronized  <T> T getObject(Class<T> cls)
            throws InstantiationException, IllegalAccessException {
        if (this.objectMap.containsKey(cls.getName())) {
            return CastUtil.cast(objectMap.get(cls.getName()));
        }
        Object object = cls.newInstance();
        object = ProxyFactory.getProxyInstance(object);
        this.objectMap.put(cls.getName(), object);
        return CastUtil.cast(object);
    }
}
