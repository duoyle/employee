package pers.xxm.empl.service.proxy;

import pers.xxm.database.ConnectionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

/**
 * Created by XuXuemin on 20/3/3
 * 动态代理，可以创建任何代理对象，这里用来实现业务层的事务提交
 */
public class ProxyFactory {
    public static Object getProxyInstance(Object target) {
        ProxyHandle proxyHandle = new ProxyHandle(target);
        // 这里不使用泛型，因为返回目标类型和传递的类型不一定一致，使用了意义不大
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), proxyHandle);
    }

    private static class ProxyHandle implements InvocationHandler {
        // 如果有多个连接需要选择，这里还需要传递连接ConnectionManager对象
        private Object target;
        private ProxyHandle(Object target) {
            this.target = target;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (!method.isAnnotationPresent(Transactional.class)) {
                try {
                    // 先执行return后的语句，结果保存，然后执行finally，接着return保存结果
                    return method.invoke(target, args); // 无返回值的返回Void类型
                } finally {
                    ConnectionManager.close(); // 如果try中System.exit(0)则不执行finally
                }
            }
            // 局部变量声明未初始化时可以的，但在使用前必须初始化
            ConnectionManager.setTransaction(true);
            try {
                Object res = method.invoke(target, args);
                ConnectionManager.commit();
                return res;
            } catch (SQLException ex){
                ConnectionManager.rollback();
                throw ex;
            } finally {
                ConnectionManager.setTransaction(false); // 恢复非事务
                ConnectionManager.close(); // 关闭连接
            }
        }
    }
}
