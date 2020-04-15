package pers.xxm.empl.dao;

import com.sun.org.apache.xpath.internal.operations.Bool;
import pers.xxm.empl.vo.Department;
import pers.xxm.empl.vo.Employee;
import pers.xxm.empl.vo.History;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao extends IDao<String, Employee> {
    /**
     * 员工离职
     *
     * @param empl 员工对象
     * @throws SQLException SQL异常
     */
    void leave(Employee empl) throws SQLException;

    /**
     * 查找对应具体员工的数量，用于判断唯一性约束（是否存在重复数据）
     *
     * @param empl    员工数据
     * @param exclude 是否排除该条数据，true排除，false不排除
     * @return 数量
     */
    long getCountByUnique(Employee empl, boolean exclude) throws SQLException;

}
