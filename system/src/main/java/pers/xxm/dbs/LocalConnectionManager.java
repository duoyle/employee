package pers.xxm.dbs;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Created by XuXuemin on 20/2/29
 */
public class LocalConnectionManager extends CommonConnManager {
    // 单例模式：一个连接管理对象，管理对应一个URL库的连接，连接可以有多个，例如多线程时，放在DataSource连接池中。
    private static LocalConnectionManager instance = new LocalConnectionManager();
    public static LocalConnectionManager getInstance() {
        return instance;
    }

    private LocalConnectionManager() {
        // 下面取代了Class.forName()（或DbUtils.loadDriver）和DriverManager.getConnection()。
        String url = "jdbc:mysql://localhost:3306/employee";
        String userName = "xxm";
        String password = "xuxuemin";
        String driverName = "com.mysql.jdbc.Driver";
        BasicDataSource source = (BasicDataSource) dataSource;
        source.setDriverClassName(driverName);
        source.setUrl(url);
        source.setUsername(userName);
        source.setPassword(password);
    }
}
