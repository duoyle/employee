package pers.xxm.empl.vo;

import lombok.Getter;
import lombok.Setter;
import pers.xxm.bean.BeanObject;

import java.io.Serializable;
import java.util.Date;

// @Data会添加toString，hashCode等方法，派生类中忽略父类的属性，同时给出警告
public class History extends BeanObject implements Serializable {
    @Getter @Setter
    private Integer changeNo;
    @Getter @Setter
    private Employee empl;
    @Getter @Setter
    private Integer salary;
    @Getter @Setter
    private Date changeDate;
    @Getter @Setter
    private String changeReason;

    public History() {
        this.empl = new Employee();
    }
}
