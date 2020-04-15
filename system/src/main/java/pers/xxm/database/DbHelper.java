package pers.xxm.database;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import pers.xxm.bean.MyBeanProcessor;
import pers.xxm.trouble.ExceptionHandler;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XuXuemin on 20/2/28
 */
public class DbHelper {

    // 根据数据库元数据生成表名、列名和列注释名
    private static Map<String, Map<String, String>> columnNames;
    static {
        try {
            columnNames = new HashMap<>();
            Connection conn = ConnectionManager.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(conn.getCatalog(), "%", "%", null); // %就是正则中的*，匹配所有
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                ResultSet columns = metaData.getColumns(conn.getCatalog(), "%", tableName, "%");
                Map<String, String> columnMap = new HashMap<>();
                while (columns.next()) {
                    // 还可以读取TYPENAME类型等内容
                    String remarks = columns.getString("REMARKS"); // comment的内容，作为中文列名
                    String columnName = columns.getString("COLUMN_NAME");
                    columnMap.put(columnName, remarks == null ? columnName : remarks);
                }
                columnNames.put(tableName, columnMap);
            }
        } catch (Exception ex) {
            ExceptionHandler.exceptionHandle(ex);
        }
    }

    /**
     * 取得对应表名和列名的中文注释列名
     * @param tableName 表名
     * @param columnName 列名
     * @return 中文注释名
     */
    public static String getColumnRemarks(String tableName, String columnName) {
        Map<String, String> columns = columnNames.get(tableName);
        if (columns == null) {
            return null;
        }
        return columns.get(columnName);
    }

    // 普通情况下：只需一个ConnectionManager。加载数据库的Driver（驱动），DbUtils.loadDriver可代替Class.forName("")。

    // QueryRunner中的方法，不关闭conn连接。内部封装了使用Statement和ResultSet等并在内部关闭了
    private static QueryRunner runner = new QueryRunner();

    // 该方法是传统的方式，不适用commons-dbutils库时的方式。这是相对优雅的方式
    @Deprecated
    private void normal(String sql) throws SQLException {
        try (Statement stat = ConnectionManager.getConnection().createStatement()) {
            try (ResultSet resultSet = stat.executeQuery(sql)) {
                resultSet.getString(1);
            }
            try (ResultSet resultSet = stat.executeQuery(sql)) {
                resultSet.getDate(1);
            }
        }
        ConnectionManager.close();
    }

    /**
     * 执行增删改操作
     * @param sql sql语句（参数化或非参数化都可）
     * @param params 参数化的参数值
     * @return 是否成功
     * @throws SQLException SQL异常
     */
    public static int update(String sql, Object... params) throws SQLException {
        return runner.update(ConnectionManager.getConnection(), sql, params);
    }

    /**
     * 查询一个列表
     * @param sql sql语句（参数化或非参数化都可）
     * @param cls 返回值列表中泛型类型，一般是表对应实体类
     * @param params 参数化的参数值
     * @return 数据列表
     * @throws SQLException SQL异常
     */
    public static <T> List<T> queryList(String sql, Class<T> cls, Object... params) throws SQLException {
        // 这里cls知道泛型T，和返回类型匹配，所以没有警告。PreparedStatement中参数化（自动加单引号？）不能含表名和字段名等
        // 反射中的Class对象判断泛型时，必须显式写出类型，否则无法判断（变成Object），此时只能强制转换（有警告）。
        RowProcessor rp = newRowProcessor();
        return runner.query(ConnectionManager.getConnection(), sql, new BeanListHandler<>(cls, rp), params);
    }

    /**
     * 查询一行数据
     * @param sql sql语句（参数化或非参数化都可）
     * @param cls 返回值列表中泛型类型，一般是表对应实体类
     * @param params 参数化的参数值
     * @return 一行数据设置好的对象
     * @throws SQLException SQL异常
     */
    public static <T> T queryLine(String sql, Class<T> cls, Object... params) throws SQLException {
        // 这里cls知道泛型T，和返回类型匹配，所以没有警告
        RowProcessor rp = newRowProcessor();
        return runner.query(ConnectionManager.getConnection(), sql, new BeanHandler<>(cls, rp), params);
    }

    /**
     * 查询一行一列数据，也就是一个值
     * @param sql sql语句（参数化或非参数化都可）
     * @param <T> 参数化的参数值
     * @return 结果值
     * @throws SQLException SQL异常
     */
    public static <T> T queryScalar(String sql, Object... params) throws SQLException {
        // ScalarHandler可设置列名或者列索引（从1开始），默认是第一列，count(*)方式结果是long
        return runner.query(ConnectionManager.getConnection(), sql, new ScalarHandler<>(), params);
    }

    // 取得行数据的处理器
    private static RowProcessor newRowProcessor() {
        // BeanHandler.handle->RowProcessor.toBean/toBeanList->BeanProcessor.toBean/toBeanList
        BeanProcessor beanProcessor = new MyBeanProcessor();
        return new BasicRowProcessor(beanProcessor);
    }
}
