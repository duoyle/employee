package pers.xxm.empl.service;

import pers.xxm.empl.vo.Employee;
import pers.xxm.empl.vo.History;

import java.sql.SQLException;
import java.util.List;

/**
 * Created By XuXuemin
 */
public interface EmployeeService extends IService<String, Employee> {
    /**
     * 新增一个员工
     * @param empl 要添加的雇员对象
     * @throws SQLException SQL异常
     */
    void insert(Employee empl) throws SQLException;

    /**
     * 修改员工信息
     * @param empl 要更新的雇员对象
     * @throws SQLException SQL异常
     */
    void update(Employee empl) throws SQLException;

    /**
     * 员工离职
     * @param key 要离职的员工编号
     * @throws SQLException SQL异常
     */
    void leave(String key) throws SQLException;

}
