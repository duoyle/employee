package pers.xxm.bean;

import pers.xxm.resource.Comments;
import pers.xxm.resource.ResourceManager;
import pers.xxm.trouble.ExceptionHandler;
import pers.xxm.util.CheckUtil;
import pers.xxm.util.StringUtil;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by XuXuemin on 20/3/5
 */
public class Validation {
    /**
     * 根据要验证的属性和包含要验证数据的请求创建验证对象
     *
     * @param necessary   要验证的必须的属性名(多个属性用|分割)
     * @param dispensable 要验证的非必须的属性名（多个属性用|分割）
     * @param request     包含数据的ServletRequest
     */
    public Validation(String necessary, String dispensable, ServletRequest request) {
        if (CheckUtil.isNotEmpty(necessary)) {
            this.necessaryParameters = necessary.split("\\|");
        }
        if (CheckUtil.isNotEmpty(dispensable)) {
            this.dispensableParameters = dispensable.split("\\|");
        }
        this.request = request;
    }

    /**
     * 根据要验证的属性和包含要验证数据的请求创建验证对象
     *
     * @param necessary 要验证的必须的属性名(多个属性用|分割)
     * @param request   包含数据的ServletRequest
     */
    public Validation(String necessary, ServletRequest request) {
        if (CheckUtil.isNotEmpty(necessary)) {
            this.necessaryParameters = necessary.split("\\|");
        }
        this.request = request;
    }

    /**
     * 根据要验证的属性和包含要验证数据的请求创建验证对象
     *
     * @param necessary   要验证的必须的属性名数组
     * @param dispensable 要验证的非必须的属性名数组
     * @param request     包含数据的ServletRequest
     */
    public Validation(String[] necessary, String[] dispensable, ServletRequest request) {
        this.necessaryParameters = necessary;
        this.dispensableParameters = dispensable;
        this.request = request;
    }

    /**
     * 根据要验证的属性和包含要验证数据的请求创建验证对象
     *
     * @param necessary 要验证的必须的属性名数组
     * @param request   包含数据的ServletRequest
     */
    public Validation(String[] necessary, ServletRequest request) {
        this.necessaryParameters = necessary;
        this.request = request;
    }

    /**
     * 验证数据是否合法
     *
     * @param bean 要验证的对象
     * @return 合法返回true，否则false
     * @throws NoSuchMethodException     未找到方法异常
     * @throws NoSuchFieldException      未找到成员异常
     * @throws InvocationTargetException 方法调用异常
     * @throws IllegalAccessException    访问权限异常
     */
    public boolean validate(Object bean) throws NoSuchMethodException, NoSuchFieldException,
            InvocationTargetException, IllegalAccessException {
        if (this.request == null) {
            return false;
        }
        if (CheckUtil.isNotEmpty(this.necessaryParameters)) {
            for (String parameter : this.necessaryParameters) {
                // 参数名parameter指定的必须是简单类型，否则使用点指向到具体属性，也就是和request参数名一致。
                if (!checkAttribute(bean, parameter, parameter)) {
                    return false;
                }
            }
        }
        if (CheckUtil.isNotEmpty(this.dispensableParameters)) {
            Enumeration<String> existParams = this.request.getParameterNames();
            while (existParams.hasMoreElements()) {
                String existParam = existParams.nextElement();
                for (String param : this.dispensableParameters) {
                    if (param.equals(existParam) && !checkAttribute(bean, param, param)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public String getMessage() {
        return this.message;
    }

    private String[] necessaryParameters;
    private String[] dispensableParameters;
    private ServletRequest request;
    private String message;

    // 递归解析数据，遇到自定义类下钻。 final变量不允许重新赋值，但可以修改其属性值
    private boolean checkAttribute(final Object bean, String attribute, String name) throws NoSuchMethodException,
            NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        String[] attributes = attribute.split("\\.", 2); // 截取第一个点分成两个，没点数组长度是1
        Class<?> beanClass = bean.getClass();
        try {
            Field field = beanClass.getDeclaredField(attributes[0]);
            if (attributes.length == 1) {
                return checkType(field.getType(), name, key -> {
                    // 设置提示信息
                    String commentName = beanClass.getSimpleName() + "." + field.getName();
                    this.message = ResourceManager.getMessage(key, Comments.get(commentName));
                    return false;
                });
            } else {
                Method getMethod = beanClass.getDeclaredMethod(BeanUtil.GET + StringUtil.initCap(attributes[0]));
                return checkAttribute(getMethod.invoke(bean), attributes[1], name);
            }
        } catch (NoSuchFieldException ex) {
            ExceptionHandler.getLogger().warn(ex.getMessage(), ex); // 不存在对应该参数的属性则放弃检查
            return true;
        }
    }

    // 按类型解析数据
    private boolean checkType(Class<?> type, String name, Predicate<String> process) {
        String typeName = type.getSimpleName();
        switch (typeName) { // 这里switch比if...else性能更好
            case "String":
                return CheckUtil.isNotEmpty(this.request.getParameter(name))
                        || process.test("validate.string");
            case "Integer":
            case "int":
                return CheckUtil.isNumber(this.request.getParameter(name))
                        || process.test("validate.int");
            case "Long":
            case "long":
                return CheckUtil.isNumber(this.request.getParameter(name))
                        || process.test("validate.long");
            case "Double":
            case "double":
                return CheckUtil.isDecimal(this.request.getParameter(name))
                        || process.test("validate.double");
            case "Date":
                return CheckUtil.isDate(this.request.getParameter(name))
                        || process.test("validate.date");
            case "String[]":
                return checkArray(this.request.getParameterValues(name), CheckUtil::isNotEmpty)
                        || process.test("validate.string");
            case "Integer[]":
            case "int[]":
                return checkArray(this.request.getParameterValues(name), CheckUtil::isNumber)
                        || process.test("validate.int");
            case "Long[]":
            case "long[]":
                return checkArray(this.request.getParameterValues(name), CheckUtil::isNumber)
                        || process.test("validate.long");
            case "Double[]":
            case "double[]":
                return checkArray(this.request.getParameterValues(name), CheckUtil::isDecimal)
                        || process.test("validate.double");
        }
        return process.test("validate");
    }

    // 验证数组所有元素是否合法
    private boolean checkArray(String[] values, Function<String, Boolean> checkFunction) {
        for (String value : values) {
            if (!checkFunction.apply(value)) {
                return false;
            }
        }
        return true;
    }

}
