package pers.xxm.empl.vo;

import pers.xxm.bean.BeanObject;

import java.io.Serializable;

public class Department extends BeanObject implements Serializable {
    // VO类中如果遇到数组需要解析的，在DB中定义Dictionary表，程序可以定义Map。
    // 对需要解析的属性设置时类型设成数组，在set方法里可split解析。
    private String deptNo;
    private String deptName;

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
