package pers.xxm.dbs;

/**
 * Created by XuXuemin on 20/2/29
 */
public class ConnectionsManager {
    /**
     * 取得本地连接管理对象
     * @return 本地连接管理对象
     */
    public static ConnectionManager getLocalConnManager() {
        return LocalConnectionManager.getInstance();
    }

    /**
     * 取得远程连接管理对象
     * @return 远程连接管理对象
     */
    public static ConnectionManager getRemoteConnManager() {
        return RemoteConnectionManager.getInstance();
    }
}
