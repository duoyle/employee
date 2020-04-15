package pers.xxm.empl.vo;

import lombok.Getter;
import lombok.Setter;
import pers.xxm.bean.BeanObject;

import java.io.Serializable;

/**
 * Created by XuXuemin on 20/4/7
 */
public class BeanTest extends BeanObject implements Serializable {
    @Getter @Setter
    private Department dept;

    @Getter @Setter
    private Department dept2;

    public BeanTest() {
        this.dept = new Department();
        this.dept2 = new Department();
    }
}
