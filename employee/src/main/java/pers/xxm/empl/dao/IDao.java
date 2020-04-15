package pers.xxm.empl.dao;

import pers.xxm.condition.Condition;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by XuXuemin on 20/2/27
 */
public interface IDao<K, V> {
    /**
     * 插入一个V对象
     *
     * @param value 要插入的对象数据
     * @throws SQLException SQL异常
     */
    void insert(V value) throws SQLException; // 据说阿里用save

    /**
     * 更新一个V对象
     *
     * @param value 要更新的对象数据
     * @throws SQLException SQL异常
     */
    void update(V value) throws SQLException;

    /**
     * 删除对应Key的数据
     *
     * @param key 对象ID
     * @throws SQLException SQL异常
     */
    void delete(K key) throws SQLException; // 据说阿里用remove

    /**
     * 分页获取数据
     *
     * @param page     当前第几页
     * @param pageSize 每页行数
     * @return 当前页数据
     */
    List<V> findByPage(long page, int pageSize) throws SQLException;

    /**
     * 分页获取数据
     *
     * @param condition 条件对象
     * @param page      当前第几页
     * @param pageSize  每页行数
     * @return 当前页数据
     */
    List<V> findByPage(Condition condition, long page, int pageSize) throws SQLException;

    /**
     * 查找所有数据
     *
     * @return 数据对象列表
     * @throws SQLException SQL异常
     */
    List<V> findAll() throws SQLException; // 据说阿里用list

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
