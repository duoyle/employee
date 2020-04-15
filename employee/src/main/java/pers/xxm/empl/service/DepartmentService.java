package pers.xxm.empl.service;

import pers.xxm.empl.vo.Department;

import java.sql.SQLException;
import java.util.List;

/**
 * Created By XuXuemin
 */
public interface DepartmentService extends IService<String, Department> {
    /**
     * 添加一个部门
     * @param dept 要添加的部门对象
     * @throws SQLException SQL异常
     */
    void insert(Department dept) throws SQLException;

    /**
     * 更新部门（无法更新编号）
     * @param dept 要更新的部门对象
     * @throws SQLException SQL异常
     */
    void update(Department dept) throws SQLException;

}
