package pers.xxm.dbs;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by XuXuemin on 20/2/29
 * ConnectionManager多个时，采取单例模式，可设置一个父类或接口，工厂类返回实例
 * DbHelper是普通创建实例模式，每个Helper中传递一个不同的ConnectionManager实例，DbHelper和C#不同，不需根据不同数据库区分
 */
public interface ConnectionManager {
    /**
     * 取得连接对象
     * @return 连接对象
     */
    Connection getConnection() throws SQLException;

    /**
     * 关闭连接
     */
    void close() throws SQLException;

    /**
     * 提交事务
     */
    void commit() throws SQLException;

    /**
     * 回滚事务
     */
    void rollback() throws SQLException;

    /**
     * 设为自动调焦
     * @param flag true是自动提交（默认）；false是事务（手动）提交
     */
    void setTransaction(boolean flag) throws SQLException;

    /**
     * 查看当前是否是事务提交
     * @return true是事务（手动）提交；false是自动提交
     */
    boolean getTransaction() throws SQLException;
}
