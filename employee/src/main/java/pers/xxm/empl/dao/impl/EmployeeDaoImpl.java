package pers.xxm.empl.dao.impl;

import pers.xxm.condition.Condition;
import pers.xxm.database.DbHelper;
import pers.xxm.empl.dao.EmployeeDao;
import pers.xxm.empl.vo.Employee;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public void insert(Employee empl) throws SQLException {
        String sql = "insert into Employee(EMPNO, DEPTNO, EMPNAME, EMPSEX, ENTRYDATE, EMPPHONE, EMPADDR, "
                + "SALARY, HOBBY, REMARK) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        DbHelper.update(sql,
                empl.getEmpNo(),
                empl.getDept().getDeptNo(),
                empl.getEmpName(),
                empl.getEmpSex(),
                new Date(empl.getEntryDate().getTime()),
                empl.getEmpPhone(),
                empl.getEmpAddr(),
                empl.getSalary(),
                empl.getHobby(),
                empl.getRemark()
        );
    }

    @Override
    public void update(Employee empl) throws SQLException {
        String sql = "update Employee set DEPTNO = ?, EMPNAME = ?, EMPSEX = ?, ENTRYDATE = ?, EMPPHONE = ?, "
                + "EMPADDR = ?, SALARY = ?, HOBBY = ?, REMARK = ? where EMPNO = ?";
        DbHelper.update(sql,
                empl.getDept().getDeptNo(),
                empl.getEmpName(),
                empl.getEmpSex(),
                new Date(empl.getEntryDate().getTime()),
                empl.getEmpPhone(),
                empl.getEmpAddr(),
                empl.getSalary(),
                empl.getHobby(),
                empl.getRemark(),
                empl.getEmpNo()
        );
    }

    @Override
    public void delete(String key) throws SQLException {
        String sql = "delete from Employee where EMPNO = ?";
        DbHelper.update(sql, key);
    }

    @Override
    public void leave(Employee empl) throws SQLException {
        String sql = "update Employee set STATE = ?, LEAVEDATE = ? where EMPNO = ?";
        DbHelper.update(sql,
                empl.getState(),
                new Date(empl.getLeaveDate().getTime()),
                empl.getEmpNo()
        );
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        // 字符串常量拼接加号在编译阶段完成；变量拼接时，连续多个加号优化成StringBuilder；循环中不会优化，建议StringBuilder
        // 兴趣嗜好复选框需要详情查看，或者不查看，修改时可看到，如果需要看到可建立视图，逗号隔开兴趣名称
        String sql = getQuerySql().concat(" order by Employee.EMPNO");
        return DbHelper.queryList(sql, Employee.class);
    }

    @Override
    public List<Employee> findByPage(long page, int pageSize) throws SQLException {
        String sql = getQuerySql().concat(" order by Employee.EMPNO limit ?, ?");
        return DbHelper.queryList(sql, Employee.class, pageSize * (page - 1), pageSize);
    }

    @Override
    public List<Employee> findByPage(Condition condition, long page, int pageSize) throws SQLException {
        String sql = getQuerySql() + " where Employee.STATE != 3 and (" + condition.getStatement()
                + ") order by Employee.EMPNO limit ?, ?";
        return DbHelper.queryList(sql, Employee.class, condition.getParameter(), pageSize * (page - 1), pageSize);
    }

    @Override
    public Employee getByKey(String key) throws SQLException {
        String sql = getQuerySql().concat(" where Employee.EMPNO = ?");
        return DbHelper.queryLine(sql, Employee.class, key);
    }

    @Override
    public long getCount() throws SQLException {
        String sql = "select count(1) from Employee";
        return DbHelper.queryScalar(sql);
    }

    @Override
    public long getCount(Condition condition) throws SQLException {
        String condStmt = condition.getStatement();
        String sql = "select count(1) from Employee ";
        if (condStmt.contains("Department")) {
            sql += "left join Department on Employee.DEPTNO=Department.DEPTNO where ";
        } else {
            sql += "where ";
        }
        sql += condStmt;
        return DbHelper.queryScalar(sql, condition.getParameter());
    }

    @Override
    public long getCountByUnique(Employee empl, boolean exclude) throws SQLException {
        String sql = "select count(1) from Employee where " + (exclude
                ? "EMPNO <> ? and EMPNAME = ?"
                : "EMPNO = ? or EMPNAME = ?");
        return DbHelper.queryScalar(sql, empl.getEmpNo(), empl.getEmpName());
    }

    // 生成基础查询SQL
    private String getQuerySql() {
        return "select Employee.EMPNO,Employee.EMPNAME,Employee.EMPSEX,(select NAME from Dictionary where CATEGORY"
                + "=11 and VALUE=Employee.EMPSEX) as EMPSEXSHOW,Employee.EMPADDR,Employee.EMPPHONE,Employee.SALARY,"
                + "Employee.ENTRYDATE,Employee.LEAVEDATE,Employee.STATE,(select NAME from Dictionary where CATEGORY"
                + "=10 and VALUE=Employee.STATE) as STATESHOW,Employee.DEPTNO,Department.DEPTNAME,Employee.HOBBY,"
                + "Employee.REMARK from Employee left join Department on Employee.DEPTNO = Department.DEPTNO";
    }
}
