package pers.xxm.bean;

import pers.xxm.trouble.ExceptionHandler;
import pers.xxm.util.CastUtil;
import pers.xxm.util.DateUtil;
import pers.xxm.util.StringUtil;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Enumeration;

/**
 * Created by XuXuemin on 20/3/25
 */
public class BeanFactory {
    // 如果Request的参数值是json字符串，则可能出现复杂情况。但一般的参数都是简单情况，要么为数组要么为简单类型值

    // form表单数据可以以json字符串提交到服务端，此时可以使用gson解析成对象
    // 如果实体类中有自定义类的数组，例如dept.deptName是个数组，dept.deptNo也是数组，但此时最好json对象接收

    private ServletRequest request; // 存储属性名和属性值

    /**
     * 构建创建实体类对象工厂实例
     *
     * @param request 属性名和属性值所在的对象
     */
    public BeanFactory(ServletRequest request) {
        this.request = request;
    }

    /**
     * 根据ServletRequest中的属性名和属性值创建对象并设置值
     *
     * @param cls 对应类型
     * @param <T> 对象类型
     * @return 创建的Bean对象
     * @throws InstantiationException    构建对象异常
     * @throws IllegalAccessException    访问权限异常
     * @throws NoSuchMethodException     方法未找到异常
     * @throws InvocationTargetException 调用异常
     * @throws ParseException            解析异常
     */
    public <T> T createObject(Class<T> cls) throws InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException, ParseException {
        T bean = cls.newInstance();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            this.setValue(bean, name, name);
        }
        return bean;
    }

    /**
     * 对现有对象按照ServletRequest中内容进行设置值
     *
     * @param bean 现有对象
     * @throws IllegalAccessException    访问权限异常
     * @throws NoSuchMethodException     方法未找到异常
     * @throws InvocationTargetException 调用异常
     * @throws ParseException            解析异常
     */
    public void setObject(final Object bean) throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException, ParseException {
        if (bean == null) {
            return;
        }
        // 如果传递时非Bean类型则会抛异常
        Enumeration<String> paramNames = this.request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            this.setValue(bean, name, name);
        }
    }

    // 递归解析数据，遇到自定义类下钻。 final变量不允许重新赋值，但可以修改其属性值
    private void setValue(final Object bean, String attribute, String name) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, ParseException {
        String[] attributes = attribute.split("\\.", 2); // 截取第一个点分成两个，没点数组长度是1
        Class<?> beanClass = bean.getClass();
        try {
            Field field = beanClass.getDeclaredField(attributes[0]);
            if (attributes.length == 1) {
                Class<?> valueType = field.getType();
                Method setMethod = beanClass.getDeclaredMethod(
                        BeanUtil.SET + StringUtil.initCap(field.getName()), valueType);
                setMethod.invoke(bean, parseValue(valueType, name));
            } else {
                Method getMethod = beanClass.getDeclaredMethod(BeanUtil.GET + StringUtil.initCap(attributes[0]));
                setValue(getMethod.invoke(bean), attributes[1], name);
            }
        } catch (NoSuchFieldException ex) {
            ExceptionHandler.getLogger().warn(ex.getMessage(), ex); // 找不到对象不设置该参数
        }
    }

    // 按类型解析数据
    private Object parseValue(Class<?> type, String name) throws ParseException {
        String typeName = type.getSimpleName();
        switch (typeName) { // 这里switch比if...else性能更好
            case "String":
                return this.request.getParameter(name);
            case "Integer":
            case "int":
                return Integer.parseInt(this.request.getParameter(name));
            case "Long":
            case "long":
                return Long.parseLong(this.request.getParameter(name));
            case "Double":
            case "double":
                return Double.parseDouble(this.request.getParameter(name));
            case "Date":
                String value = this.request.getParameter(name);
                if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    return DateUtil.parseDate(value);
                } else if (value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                    return DateUtil.parseTime(value);
                }
                break;
            case "String[]":
                return CastUtil.parseArray(this.request.getParameterValues(name), x -> x);
            case "Integer[]":
                return CastUtil.parseArray(this.request.getParameterValues(name), Integer::parseInt);
            case "Long[]":
                return CastUtil.parseArray(this.request.getParameterValues(name), Long::parseLong);
            case "Double[]":
                return CastUtil.parseArray(this.request.getParameterValues(name), Double::parseDouble);
            case "int[]":
                return CastUtil.parseIntArray(this.request.getParameterValues(name));
            case "long[]":
                return CastUtil.parseLongArray(this.request.getParameterValues(name));
            case "double[]":
                return CastUtil.parseDoubleArray(this.request.getParameterValues(name));
        }
        return null;
    }
}
