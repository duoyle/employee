package pers.xxm.dbs;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by XuXuemin on 20/2/29
 */
public abstract class CommonConnManager implements ConnectionManager {
    // 下面是非静态部分
    private ThreadLocal<Connection> remoteConnection = new ThreadLocal<>();
    DataSource dataSource = new BasicDataSource();

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = remoteConnection.get();
        if (conn == null) {
            conn = dataSource.getConnection();
            remoteConnection.set(conn);
        }
        return conn;
    }

    @Override
    public void close() throws SQLException {
        this.getConnection().close();
        remoteConnection.remove(); // 移除掉，或者set(null)
    }

    @Override
    public void commit() throws SQLException {
        this.getConnection().commit();
    }

    @Override
    public void rollback() throws SQLException {
        this.getConnection().rollback();
    }

    @Override
    public void setTransaction(boolean flag) throws SQLException {
        this.getConnection().setAutoCommit(flag);
    }

    @Override
    public boolean getTransaction() throws SQLException {
        return !this.getConnection().getAutoCommit();
    }
}
