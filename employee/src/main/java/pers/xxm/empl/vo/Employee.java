package pers.xxm.empl.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pers.xxm.bean.BeanObject;
import pers.xxm.empl.common.Constant;
import pers.xxm.util.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee extends BeanObject implements Serializable {
    @Getter
    @Setter
    private String empNo;
    @Getter
    @Setter
    private Department dept;
    @Getter
    @Setter
    private String empName;
    @Getter
    @Setter
    private Integer empSex;
    @Getter
    @Setter
    private String empSexShow;
    @Getter
    @Setter
    private Date entryDate; // java.sql.Date继承自java.util.Date
    @Getter
    @Setter
    private String empPhone;
    @Getter
    @Setter
    private String empAddr;
    @Getter
    @Setter
    private Integer salary;
    @Getter
    @Setter
    private Date leaveDate;
    @Getter
    private Long hobby;
    @Getter
    @Setter
    private String hobbyShow;
    @Getter
    private Long[] hobbies;
    @Getter
    @Setter
    private String remark;
    @Getter
    @Setter
    private Integer state; // 1在职, 2离职, 4减员
    @Getter
    @Setter
    private String stateShow;

    public Employee() {
        this.dept = new Department();
    }

    // 后台设置兴趣
    public void setHobby(Long hobby) {
        List<Long> list = new ArrayList<>();
        long l = 1;
        while (l <= hobby) {
            if ((l & hobby) == l) {
                list.add(l);
            }
            l <<= 1;
        }
        this.hobbies = new Long[list.size()];
        list.toArray(this.hobbies);
        this.hobby = hobby;
    }

    // 前台设置兴趣
    public void setHobbies(Long[] hobbies) {
        if (hobbies == null) {
            return;
        }
        long result = 0;
        for (Long hobby : hobbies) {
            result |= hobby;
        }
        this.hobby = result;
        this.hobbies = hobbies;
    }

}
