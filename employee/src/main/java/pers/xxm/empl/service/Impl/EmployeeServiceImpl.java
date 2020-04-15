package pers.xxm.empl.service.Impl;

import pers.xxm.condition.Condition;
import pers.xxm.condition.ConditionFactory;
import pers.xxm.empl.common.Constant;
import pers.xxm.empl.dao.EmployeeDao;
import pers.xxm.empl.dao.HistoryDao;
import pers.xxm.empl.dao.factory.DaoFactory;
import pers.xxm.empl.dao.impl.EmployeeDaoImpl;
import pers.xxm.empl.dao.impl.HistoryDaoImpl;
import pers.xxm.empl.service.EmployeeService;
import pers.xxm.empl.service.IService;
import pers.xxm.empl.service.proxy.Transactional;
import pers.xxm.empl.vo.Employee;
import pers.xxm.empl.vo.History;
import pers.xxm.empl.vo.Page;
import pers.xxm.resource.Comments;
import pers.xxm.resource.ResourceManager;
import pers.xxm.trouble.BusinessException;
import pers.xxm.trouble.ErrorCode;
import pers.xxm.util.StringUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeDao emplDao;
    private HistoryDao histDao;

    public EmployeeServiceImpl() throws InstantiationException, IllegalAccessException {
        emplDao = DaoFactory.getFactory().getObject(EmployeeDaoImpl.class);
        histDao = DaoFactory.getFactory().getObject(HistoryDaoImpl.class);
    }

    @Override
    @Transactional
    public void insert(Employee empl) throws SQLException {
        if (emplDao.getCountByUnique(empl, false) > 0) {
            throw new BusinessException(ErrorCode.EXISTS,
                    ResourceManager.getMessage("exists", Comments.get("Employee")));
        }
        History history = new History();
        history.setEmpl(empl);
        history.setSalary(empl.getSalary());
        history.setChangeDate(new Date());
        history.setChangeReason("新员工入职");
        emplDao.insert(empl);
        histDao.insert(history);
    }

    @Override
    @Transactional
    public void update(Employee empl) throws SQLException {
        if (emplDao.getCountByUnique(empl, true) > 0) {
            throw new BusinessException(ErrorCode.EXISTS,
                    ResourceManager.getMessage("exists", Comments.get("Employee")));
        }
        History history = new History();
        history.setEmpl(empl);
        history.setSalary(empl.getSalary());
        history.setChangeDate(new Date());
        history.setChangeReason("员工信息修改");
        emplDao.update(empl);
        histDao.insert(history);
    }

    @Override
    @Transactional
    public void leave(String key) throws SQLException {
        Employee empl = new Employee();
        empl.setEmpNo(key);
        empl.setState(Constant.LEAVE_JOB);
        empl.setLeaveDate(new Date());

        History history = new History();
        history.setEmpl(empl);
        history.setSalary(empl.getSalary());
        history.setChangeDate(empl.getLeaveDate());
        history.setChangeReason("员工离职");
        emplDao.leave(empl);
        histDao.insert(history);
    }

    @Override
    @Transactional
    public void delete(String key) throws SQLException {
        histDao.deleteByEmpl(key);
        emplDao.delete(key);
    }

    @Override
    public List<Employee> listAll() throws SQLException {
        return emplDao.findAll();
    }

    @Override
    public void listInPage(final Page<Employee> page) throws SQLException {
        IService.listInPage(this.emplDao, page);
    }

    @Override
    public Employee getByKey(String empNo) throws SQLException {
        return emplDao.getByKey(empNo);
    }

    @Override
    public long getCount() throws SQLException {
        return emplDao.getCount();
    }

    @Override
    public long getCount(Condition condition) throws SQLException {
        return emplDao.getCount(condition);
    }

}
