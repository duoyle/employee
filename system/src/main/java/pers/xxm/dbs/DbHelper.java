package pers.xxm.dbs;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by XuXuemin on 20/2/29
 * 在DAO层中，实现类中创建一个DbHelper实例，传递所需要的ConnectionManager实例。因为DAO中每个表知道在哪个连接对应的库里
 */
public class DbHelper {
    // QueryRunner中的方法，不关闭conn连接。内部封装了使用Statement和ResultSet等并在内部关闭了
    private QueryRunner runner;
    private ConnectionManager connManager;

    public DbHelper(ConnectionManager connManager) {
        this.connManager = connManager;
        runner = new QueryRunner();
    }

    /**
     * 执行增删改操作
     * @param sql sql语句（参数化或非参数化都可）
     * @param params 参数化的参数值
     * @return 是否成功
     * @throws SQLException SQL异常
     */
    public boolean update(String sql, Object... params) throws SQLException {
        try {
            int num = runner.update(connManager.getConnection(), sql, params);
            return num > 0;
        } finally {
            if (!connManager.getTransaction()) connManager.close();
        }
    }

    /**
     * 查询一个列表
     * @param sql sql语句（参数化或非参数化都可）
     * @param cls 返回值列表中泛型类型，一般是表对应实体类
     * @param params 参数化的参数值
     * @return 数据列表
     * @throws SQLException SQL异常
     */
    public <T> List<T> queryList(String sql, Class<T> cls, Object... params) throws SQLException {
        try {
            // 这里cls知道泛型T，和返回类型匹配，所以没有警告
            return runner.query(connManager.getConnection(), sql, new BeanListHandler<>(cls), params);
        } finally {
            if (!connManager.getTransaction()) connManager.close();
        }
    }

    /**
     * 查询一行数据
     * @param sql sql语句（参数化或非参数化都可）
     * @param cls 返回值列表中泛型类型，一般是表对应实体类
     * @param params 参数化的参数值
     * @return 一行数据设置好的对象
     * @throws SQLException SQL异常
     */
    public <T> T queryLine(String sql, Class<T> cls, Object... params) throws SQLException {
        try {
            // 这里cls知道泛型T，和返回类型匹配，所以没有警告
            return runner.query(connManager.getConnection(), sql, new BeanHandler<>(cls), params);
        } finally {
            if (!connManager.getTransaction()) connManager.close();
        }
    }

    /**
     * 查询一行一列数据，也就是一个值
     * @param sql sql语句（参数化或非参数化都可）
     * @param cls 返回值的类型（简单类型用包装类）
     * @param <T> 参数化的参数值
     * @return 结果值
     * @throws SQLException SQL异常
     */
    public <T> T queryScalar(String sql, Class<T> cls, Object... params) throws SQLException {
        try {
            return runner.query(connManager.getConnection(), sql, new ScalarHandler<T>(), params);
        } finally {
            if (!connManager.getTransaction()) connManager.close();
        }
    }
}
