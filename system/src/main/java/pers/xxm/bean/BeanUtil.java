package pers.xxm.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pers.xxm.util.DateUtil;
import pers.xxm.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;

/**
 * Created by XuXuemin on 20/3/24
 * 不要包含自定义类数组属性（此种方式可采取多个结果对象方式，若真有此种数组可单独处理）
 */
public class BeanUtil {
    // 一种改良设计：验证和解析都继承自一个接口，分别对应每种类型的方法，验证实现验证部分，解析实现解析部分
    // 如此，在switch匹配类型中可缩减成一个方法，相当于函数式接口中嵌套了函数式接口
    public static final String GET = "get";
    public static final String SET = "set";

    /**
     * 根据对象设置request的属性值
     * @param request 要设置的request
     * @param bean 包含值的对象
     * @throws IllegalAccessException 访问异常
     */
    public static void setRequest(final HttpServletRequest request, Object bean) throws IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        if (request == null || bean == null) {
            return;
        }
        // 如果传递时非Bean类型则会抛异常
        // 因为EL表达式是服务端处理，所以设置值较简单
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field: fields) {
            String methodName = BeanUtil.GET + StringUtil.initCap(field.getName());
            Method method = beanClass.getDeclaredMethod(methodName);
            Object result = method.invoke(bean);
            request.setAttribute(field.getName(), result);
        }
    }

}
