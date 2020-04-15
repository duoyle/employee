package pers.xxm.bean;

import lombok.Getter;
import org.apache.commons.dbutils.ColumnHandler;
import org.apache.commons.dbutils.PropertyHandler;
import org.apache.commons.dbutils.handlers.properties.StringEnumPropertyHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ServiceLoader;

/**
 * Created by XuXuemin on 20/4/6
 */
public class ColumnProcessor implements Cloneable {
    /**
     * ServiceLoader to find <code>ColumnHandler</code> implementations on the classpath.  The iterator for this is
     * lazy and each time <code>iterator()</code> is called.
     */
    // FIXME: I think this instantiates new handlers on each iterator() call. This might be worth caching upfront.
    protected static final ServiceLoader<ColumnHandler> columnHandlers = ServiceLoader.load(ColumnHandler.class);

    private ColumnProcessor target; // 指向的目标列处理器，用于处理嵌套bean对象
    public void setTarget(ColumnProcessor target) {
        this.target = target;
        target.upper = this;
    }

    @Getter
    private ColumnProcessor upper; // 上一层

    private PropertyDescriptor propertyDescriptor;

    private ColumnHandler columnHandler; // 处理列数据，就是根据具体类型get具体类型的值（如getString），有时可省
    // 如果setter参数类型和列实际值不一致需要propertyHandler做转化
    private PropertyHandler propertyHandler; // 如果属性有枚举类型，需要ResultSet中的值转为对应枚举值

    public ColumnProcessor(PropertyDescriptor propertyDescriptor) throws SQLException {
        this.propertyDescriptor = propertyDescriptor;
        initialize();
    }
    private Method setter; // 创建好，解析时直接invoke调用
    private Method getter; // 当target不空时使用
    private Class<?> propType; // setter方法的参数和属性类型一致

    private void initialize() throws SQLException {
        // 设置要将ResultSet中的值处理成对应属性类型的处理器
        this.propType = propertyDescriptor.getPropertyType();
        for (ColumnHandler handler : columnHandlers) {
            if (handler.match(propType)) {
                this.columnHandler = handler;
                break;
            }
        }

        // 设置value值和setter参数的对应是否需要处理
        this.getter = propertyDescriptor.getReadMethod();
        this.setter = propertyDescriptor.getWriteMethod();
        Class<?> firstParam = setter.getParameterTypes()[0];
        if (firstParam.isEnum()) { // 日期转换暂时不用，具体参照PropertyHandler的实现类
            propertyHandler = new StringEnumPropertyHandler();
        }
    }

    // 设置值到bean属性
    public <T> void process(T bean, ResultSet resultSet, int index) throws SQLException {
        try {
            if (target == null) {
                Object value = parseColumn(resultSet, index);
                callSetter(bean, value);
            } else {
                Object prop = getter.invoke(bean);
                target.process(prop, resultSet, index);
            }
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new SQLException("can not find getter method of " + propType.getName() + ": " + ex.getMessage());
        }
    }

    // 解析对应列的数据
    private Object parseColumn(ResultSet rs, int index) throws SQLException {
        Object retval = rs.getObject(index);
        if (!this.propType.isPrimitive() && retval == null) {
            return null; // 自定义类型时值为空不处理
        }
        if (columnHandler != null) {
            retval = columnHandler.apply(rs, index);
        }
        return retval;
    }

    // 调用setter方法赋值
    private <T> void callSetter(T bean, Object value) throws SQLException {
        try {
            if (propertyHandler != null) {
                value =  propertyHandler.apply(propType, value);
            }
            setter.invoke(bean, value); // 省略了一些参数的匹配检查

        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            throw new SQLException("Cannot set " + propertyDescriptor.getName() + ": " + ex.getMessage());
        }
    }

    // 如果构造方法中需要执行内容则clone比new效率高，否则new优化后的效率似乎更高
    @Override
    public ColumnProcessor clone() throws CloneNotSupportedException {
        return (ColumnProcessor) super.clone();
    }
}
