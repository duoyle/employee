package pers.xxm.empl.dao.impl;

import pers.xxm.condition.Condition;
import pers.xxm.database.DbHelper;
import pers.xxm.empl.dao.HistoryDao;
import pers.xxm.empl.vo.History;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class HistoryDaoImpl implements HistoryDao {
    @Override
    public void insert(History value) throws SQLException {
        String sql = "insert into History(EMPNO, SALARY, CHANGEDATE, CHANGEREASON) values(?, ?, ?, ?)";
        DbHelper.update(sql,
                value.getEmpl().getEmpNo(),
                value.getSalary(),
                new Date(value.getChangeDate().getTime()),
                value.getChangeReason()
        );
    }

    @Override
    public void update(History value) throws SQLException {
        String sql = "insert into History(EMPNO, SALARY, CHANGEDATE, CHANGEREASON) values(?, ?, ?, ?)";
        DbHelper.update(sql,
                value.getEmpl().getEmpNo(),
                value.getSalary(),
                new Date(value.getChangeDate().getTime()),
                value.getChangeReason()
        );
    }

    @Override
    public void delete(Integer key) throws SQLException {
        String sql = "delete from History where CHANGENO = ?";
        DbHelper.update(sql, key);
    }

    @Override
    public void deleteByEmpl(String empNo) throws SQLException {
        String sql = "delete from History where EMPNO = ?";
        DbHelper.update(sql, empNo);
    }

    @Override
    public History getByKey(Integer key) throws SQLException {
        String sql = "select H.CHANGENO,H.EMPNO,E.EMPNAME,E.EMPSEX,E.EMPADDR,E.EMPPHONE,E.SALARY,E.ENTRYDATE,"
                + "E.LEAVEDATE,E.STATE,E.DEPTNO,D.DEPTNAME from (select * from History where CHANGENO = ?) H "
                + "left join Employee E on H.EMPNO = E.EMPN left join Department D on E.DEPTNO = D.DEPTNO";
        return DbHelper.queryLine(sql, History.class, key);
    }

    @Override
    public List<History> findAll() throws SQLException {
        String sql = getQuerySql();
        return DbHelper.queryList(sql, History.class);
    }

    @Override
    public List<History> findByPage(long page, int pageSize) throws SQLException {
        String sql = getQuerySql().concat(" limit ?, ?");
        return DbHelper.queryList(sql, History.class, pageSize * (page - 1), pageSize);
    }

    @Override
    public List<History> findByPage(Condition condition, long page, int pageSize) throws SQLException {
        String sql = getQuerySql() + " where " + condition.getStatement() + " limit ?, ?";
        return DbHelper.queryList(sql, History.class, condition.getParameter(), pageSize * (page - 1), pageSize);
    }

    @Override
    public List<History> search(Condition condition) throws SQLException {
        String sql = getQuerySql() + " where " + condition.getStatement();
        return DbHelper.queryList(sql, History.class, condition.getParameters());
    }

    @Override
    public long getCount() throws SQLException {
        String sql = "select count(1) from History";
        return DbHelper.queryScalar(sql);
    }

    @Override
    public long getCount(Condition condition) throws SQLException {
        String condStmt = condition.getStatement();
        String sql = "select count(1) from History left join Employee on History.EMPNO = Employee.EMPNO ";
        if (condStmt.contains("Department")) {
            sql += "left join Department on Employee.DEPTNO=Department.DEPTNO where ";
        } else {
            sql += "where ";
        }
        sql += condStmt;
        return DbHelper.queryScalar(sql, condition.getParameter());
    }

    // 取得基础查询sql语句
    private String getQuerySql() {
        return "select History.CHANGENO,History.EMPNO,Employee.EMPNAME,Employee.EMPSEX,(select NAME from "
                + "Dictionary where CATEGORY=11 and VALUE=Employee.EMPSEX) as EMPSEXSHOW,Employee.EMPPHONE,"
                + "Employee.SALARY,Employee.ENTRYDATE,Employee.LEAVEDATE,Employee.STATE,(select NAME from "
                + "Dictionary where CATEGORY = 10 and VALUE=Employee.STATE) as STATESHOW,Employee.DEPTNO,"
                + "Department.DEPTNAME,History.CHANGEDATE,History.CHANGEREASON from History left join Employee "
                + "on History.EMPNO=Employee.EMPNO left join Department on Employee.DEPTNO=Department.DEPTNO";
    }

}
