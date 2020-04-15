package pers.xxm.empl.service.proxy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by XuXuemin on 20/3/3 标示方法是否需要事务
 * Inherited 叫元注解，使用该注解的自定义注解用在父类上则子类也有该注解，子类若没有覆写方法A则A也有该注解，覆写则没有
 * 这里要定义成RUNTIME范围，否则警告，说是没有采用反射访问
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
}
