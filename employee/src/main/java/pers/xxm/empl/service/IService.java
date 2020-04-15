package pers.xxm.empl.service;

import pers.xxm.condition.Condition;
import pers.xxm.condition.ConditionFactory;
import pers.xxm.empl.dao.IDao;
import pers.xxm.empl.vo.Page;
import pers.xxm.resource.ResourceManager;
import pers.xxm.trouble.BusinessException;
import pers.xxm.trouble.ErrorCode;
import pers.xxm.util.StringUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by XuXuemin on 20/3/2
 * 业务层设置异常信息并抛出，一般在控制层进行处理
 */
public interface IService<K, V> {
    /**
     * 删除对应Key的数据
     *
     * @param key 对象ID
     * @throws SQLException SQL异常
     */
    void delete(K key) throws SQLException; // 据说阿里用remove

    /**
     * 分页获取数据，数据设置到page对象
     *
     * @param page 包含参数的页数据
     */
    void listInPage(final Page<V> page) throws SQLException;

    /**
     * 分页读取数据，这是提供给实现类调用的，默认分页方法
     *
     * @param dao  读取数据的dao实例
     * @param page page数据
     * @param <T>  数据实体类泛型
     * @throws SQLException SQL异常
     */
    static <T> void listInPage(IDao<?, T> dao, final Page<T> page) throws SQLException {
        if (page == null) {
            throw new BusinessException(ErrorCode.PARAM_ILLEGAL, ResourceManager.getMessage("param.illegal"));
        }
        if (StringUtil.isEmpty(page.getKeyword())) {
            page.setTotalRows(dao.getCount());
            page.setRows(dao.findByPage(page.getCurrentPage(), page.getPageSize()));
        } else {
            Condition condition = ConditionFactory.newCondition(page.getColumn(), page.getKeyword());
            page.setTotalRows(dao.getCount(condition));
            page.setRows(dao.findByPage(condition, page.getCurrentPage(), page.getPageSize()));
        }
    }

    /**
     * 查找所有数据
     *
     * @return 数据对象列表
     * @throws SQLException SQL异常
     */
    List<V> listAll() throws SQLException; // 据说阿里用list

    /**
     * 获取一行数据
     *
     * @param key 要获取数据的Key（id）
     * @return 该行数据生成的V对象
     * @throws SQLException SQL异常
     */
    V getByKey(K key) throws SQLException;

    /**
     * 取得数据行数
     *
     * @return 行数
     * @throws SQLException SQL异常
     */
    long getCount() throws SQLException;

    /**
     * 取得数据行数
     *
     * @param condition 条件对象
     * @return 行数
     * @throws SQLException SQL异常
     */
    long getCount(Condition condition) throws SQLException;

}
