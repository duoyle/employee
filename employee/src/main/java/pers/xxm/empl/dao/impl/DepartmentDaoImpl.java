package pers.xxm.empl.dao.impl;

import pers.xxm.condition.Condition;
import pers.xxm.empl.dao.DepartmentDao;
import pers.xxm.database.DbHelper;
import pers.xxm.empl.vo.Department;

import java.sql.SQLException;
import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {
    @Override
    public void insert(Department dept) throws SQLException {
        String sql = "insert into Department(DEPTNO, DEPTNAME) values(?, ?)";
        DbHelper.update(sql, dept.getDeptNo(), dept.getDeptName());
    }

    @Override
    public void update(Department dept) throws SQLException {
        String sql = "update Department set DEPTNAME = ? where DEPTNO = ?";
        DbHelper.update(sql, dept.getDeptName(), dept.getDeptNo());
    }

    @Override
    public void delete(String key) throws SQLException {
        String sql = "delete from Department where DEPTNO = ?";
        DbHelper.update(sql, key);
    }

    @Override
    public List<Department> findByPage(long page, int pageSize) throws SQLException {
        String sql = "select DEPTNO,DEPTNAME from Department order by DEPTNO limit ?, ?";
        return DbHelper.queryList(sql, Department.class, pageSize * (page - 1), pageSize);
    }

    @Override
    public List<Department> findByPage(Condition condition, long page, int pageSize) throws SQLException {
        String sql = "select DEPTNO,DEPTNAME from Department where "
                + condition.getStatement() + " order by DEPTNO limit ?, ?";
        return DbHelper.queryList(sql, Department.class, condition.getParameter(), pageSize * (page - 1), pageSize);
    }

    @Override
    public List<Department> findAll() throws SQLException {
        String sql = "select DEPTNO,DEPTNAME from Department order by DEPTNO";
        return DbHelper.queryList(sql, Department.class);
    }

    @Override
    public Department getByKey(String key) throws SQLException {
        String sql = "select DEPTNO,DEPTNAME from Department where DEPTNO = ?";
        return DbHelper.queryLine(sql, Department.class, key);
    }

    @Override
    public long getCount() throws SQLException {
        String sql = "select count(1) from Department";
        return DbHelper.queryScalar(sql);
    }

    @Override
    public long getCount(Condition condition) throws SQLException {
        String sql = "select count(1) from Department where " + condition.getStatement();
        return DbHelper.queryScalar(sql, condition.getParameter());
    }

    @Override
    public long getCountByUnique(Department dept, boolean exclude) throws SQLException {
        String sql = "select count(1) from Department where " + (exclude
                ? "DEPTNO <> ? and DEPTNAME = ?"
                : "DEPTNO = ? or DEPTNAME = ?");
        return DbHelper.queryScalar(sql, dept.getDeptNo(), dept.getDeptName());
    }

}
