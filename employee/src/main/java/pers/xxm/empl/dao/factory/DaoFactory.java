package pers.xxm.empl.dao.factory;

import pers.xxm.empl.factory.Factory;
import pers.xxm.empl.dao.DepartmentDao;
import pers.xxm.empl.dao.EmployeeDao;
import pers.xxm.empl.dao.HistoryDao;
import pers.xxm.empl.dao.impl.DepartmentDaoImpl;
import pers.xxm.empl.dao.impl.EmployeeDaoImpl;
import pers.xxm.empl.dao.impl.HistoryDaoImpl;
import pers.xxm.util.CastUtil;

/**
 * Created by XuXuemin on 20/3/1
 */
public class DaoFactory extends Factory {
    private volatile static Factory factory;
    public static Factory getFactory() {
        if (factory == null) {
            synchronized (DaoFactory.class) {
                if (factory == null) {
                    factory = new DaoFactory();
                }
            }
        }
        return factory;
    }

    @Override
    public synchronized <T> T getObject(Class<T> cls)
            throws InstantiationException, IllegalAccessException {
        if (this.objectMap.containsKey(cls.getName())) {
            return CastUtil.cast(objectMap.get(cls.getName()));
        }
        Object object = cls.newInstance();
        this.objectMap.put(cls.getName(), object);
        return CastUtil.cast(object);
    }

    // 下方暂时未用

    /**
     * 创建部门DAO层实例
     * @return 部门DAO层实例
     */
    public static DepartmentDao getDeptDaoInstance() {
        return new DepartmentDaoImpl();
    }

    /**
     * 创建员工DAO层实例
     * @return 员工DAO层实例
     */
    public static EmployeeDao getEmplDaoInstance() {
        return new EmployeeDaoImpl();
    }

    /**
     * 创建历史记录DAO层实例
     * @return 历史记录DAO层实例
     */
    public static HistoryDao getHistDaoInstance() {
        return new HistoryDaoImpl();
    }

}
