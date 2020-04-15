package pers.xxm.empl.dao;

import pers.xxm.empl.vo.Department;
import pers.xxm.empl.vo.Employee;

import java.sql.SQLException;
import java.util.List;

public interface DepartmentDao extends IDao<String, Department> {

    /**
     * 查找对应具体部门的数量，用于判断唯一性约束（是否存在重复数据）
     *
     * @param dept    部门数据
     * @param exclude 是否排除该条数据，true排除，false不排除
     * @return 数量
     */
    long getCountByUnique(Department dept, boolean exclude) throws SQLException;

}
