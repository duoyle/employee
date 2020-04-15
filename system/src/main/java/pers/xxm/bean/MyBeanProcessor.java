package pers.xxm.bean;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.RowProcessor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XuXuemin on 20/4/5
 */
public class MyBeanProcessor extends BeanProcessor {

    // 数据库字段名称使用下划线连接单词的字段，可用GenerousBeanProcessor代替BeanProcessor
    public static RowProcessor getProcessor() {
        Map<String, String> columnToProperty = new HashMap<>();
        columnToProperty.put("DEPTNO", "dept.deptNo");
        columnToProperty.put("DEPTNAME", "dept.deptName");
        BeanProcessor processor = new BeanProcessor(columnToProperty);
        return new BasicRowProcessor(processor);
    }

    public MyBeanProcessor() {
        super();
    }
    /* 暂时不接受指定列名和属性名的对应
    public MyBeanProcessor(Map<String, String> columnToPropertyOverrides) {
        super(columnToPropertyOverrides);
    }
    */

    /* ******************* xxm edit ******************** */

    /**
     * 转为Bean实体类对象
     * @param rs 结果集
     * @param type bean类型
     * @param <T> bean的泛型类
     * @return bean对象
     * @throws SQLException SQL异常
     */
    @Override
    public <T> T toBean(ResultSet rs, Class<? extends T> type) throws SQLException {
        // 调用处已经执行了rs.next()判断了
        this.beanType = type;
        initColumnProcessor(rs); // 初始化列处理程式和保存列名
        populateBean(type, null); // 生成解析数据的具体处理程式
        return this.createBean(rs, type);
    }

    /**
     * 转为Bean实体类对象列表
     * @param rs 结果集
     * @param type bean类型
     * @param <T> bean的泛型类
     * @return bean对象列表
     * @throws SQLException SQL异常
     */
    @Override
    public <T> List<T> toBeanList(ResultSet rs, Class<? extends T> type) throws SQLException {
        List<T> results = new ArrayList<>();
        if (!rs.next()) { // 防止没有东西，可提高性能
            return results;
        }
        this.beanType = type;
        initColumnProcessor(rs); // 初始化列处理程式和保存列名
        populateBean(type, null); // 生成解析数据的具体处理程式
        do {
            results.add(this.createBean(rs, type));
        } while (rs.next());

        return results;
    }

    // 初始化每列对应的处理器，按照ResultSet结果，每列一个
    private void initColumnProcessor(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsMetaData = resultSet.getMetaData();
        int colCount = rsMetaData.getColumnCount();
        String[] colNames = new String[colCount + 1]; // 序号从1开始，第0个空置
        for (int i = 1; i <= colCount; i++) {
            String colName = rsMetaData.getColumnLabel(i);
            if (colName == null) {
                colName = rsMetaData.getColumnName(i);
            }
            colNames[i] = colName;
        }
        this.columnNames = colNames;
        this.columnProcessors = new ColumnProcessor[colCount + 1]; // 序号从1开始，第0个空置
    }

    // 创建一个bean实例，并赋值
    private <T> T createBean(ResultSet rs, Class<T> type) throws SQLException {
        T bean = newInstance(type);
        for (int i = 1; i < columnProcessors.length; i++) {
            columnProcessors[i].process(bean, rs, i);
        }
        return bean;
    }

    private ColumnProcessor[] columnProcessors; // 每列对应的处理器，保存的处理结构
    private String[] columnNames; // 每列的列名，顺序正好和ResultSet中列的顺序一致，第一个空出
    private Class<?> beanType; // 保存的bean类型，暂时用于判断是否有循环引用（此时导致死循环）

    // 递归遍历生成列处理器，列名不包含点，直接使用属性名，所以最好表中列名跨表不要重复
    private <T> void populateBean(Class<T> type, ColumnProcessor cp) throws SQLException {
        PropertyDescriptor[] propDescriptors = getPropertyDescriptors(type);
        try {
            for (PropertyDescriptor propDescriptor : propDescriptors) {
                Class<?> propType = propDescriptor.getPropertyType();
                if (propType.getName().equals(this.beanType.getName())) {
                    throw new SQLException("bean class reference itself"); // 引用自己导致死循环
                }
                if (BeanObject.class.isAssignableFrom(propType)) {
                    ColumnProcessor cpCurrent = new ColumnProcessor(propDescriptor);
                    if (cp != null) {
                        cp.setTarget(cpCurrent);
                    }
                    populateBean(propType, cpCurrent);
                } else {
                    for (int i = 1; i < columnNames.length; i++) {
                        if (propDescriptor.getName().equalsIgnoreCase(this.columnNames[i])) {
                            // 如果该列已经设置过了则不再设置，例如外层类属性名匹配上了，属性类型里的属性不再设置
                            if (columnProcessors[i] != null && cp != null) {
                                break; // 第一层属性优先级最高，其他谁排在前谁最优先
                            }
                            ColumnProcessor cpCurrent = new ColumnProcessor(propDescriptor);
                            columnProcessors[i] = getColumnProcessor(cp, cpCurrent); // 保存第一层属性
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 取得属性描述信息实例
    private PropertyDescriptor[] getPropertyDescriptors(Class<?> c) throws SQLException {
        // Introspector caches BeanInfo classes for better performance
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(c);
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new SQLException("Bean introspection failed: " + e.getMessage());
        }
    }

    // 如果存在上层则克隆一个，防止同一个target赋给一个columnProcessor，克隆的PropertyDescriptor指向同一个（浅度克隆）
    private ColumnProcessor getColumnProcessor(ColumnProcessor cp, ColumnProcessor target) throws SQLException {
        if (cp == null) {
            return target;
        }
        try {
            cp = cp.clone();
            cp.setTarget(target);
            return getColumnProcessor(cp.getUpper(), cp);
        } catch (CloneNotSupportedException ex) {
            throw new SQLException("can not copy column processor: " + ex.getMessage());
        }
    }

    @Override
    protected <T> T newInstance(Class<T> type) throws SQLException {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new SQLException("Can not create " + type.getName() + ": " + ex.getMessage());
        }
    }

}
