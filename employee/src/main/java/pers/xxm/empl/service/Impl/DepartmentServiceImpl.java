package pers.xxm.empl.service.Impl;

import pers.xxm.condition.Condition;
import pers.xxm.empl.service.IService;
import pers.xxm.empl.vo.Page;
import pers.xxm.resource.Comments;
import pers.xxm.trouble.BusinessException;
import pers.xxm.trouble.ErrorCode;
import pers.xxm.empl.dao.DepartmentDao;
import pers.xxm.empl.dao.factory.DaoFactory;
import pers.xxm.empl.dao.impl.DepartmentDaoImpl;
import pers.xxm.resource.ResourceManager;
import pers.xxm.empl.vo.Department;
import pers.xxm.empl.service.DepartmentService;
import pers.xxm.util.StringUtil;

import java.sql.SQLException;
import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentDao deptDao; // 使用DAO而不是DAOImpl，上溯造型

    public DepartmentServiceImpl() throws InstantiationException, IllegalAccessException {
        deptDao = DaoFactory.getFactory().getObject(DepartmentDaoImpl.class);
    }

    @Override
    public void insert(Department dept) throws SQLException {
        if (deptDao.getCountByUnique(dept, false) > 0) {
            throw new BusinessException(ErrorCode.EXISTS,
                    ResourceManager.getMessage("exists", Comments.get("Department")));
        }
        deptDao.insert(dept);
    }

    @Override
    public void update(Department dept) throws SQLException {
        if (deptDao.getCountByUnique(dept, true) > 0) {
            throw new BusinessException(ErrorCode.EXISTS,
                    ResourceManager.getMessage("exists", Comments.get("Department")));
        }
        deptDao.update(dept);
    }

    @Override
    public void delete(String deptNo) throws SQLException {
        deptDao.delete(deptNo);
    }

    @Override
    public void listInPage(final Page<Department> page) throws SQLException {
        IService.listInPage(this.deptDao, page);
    }

    @Override
    public List<Department> listAll() throws SQLException {
        return deptDao.findAll();
    }

    @Override
    public Department getByKey(String key) throws SQLException {
        if (StringUtil.isEmpty(key)) {
            throw new BusinessException(ErrorCode.PARAM_ILLEGAL, ResourceManager.getMessage("param.illegal"));
        }
        Department result = deptDao.getByKey(key);
        if (result == null) {
            throw new BusinessException(ErrorCode.NOT_EXISTS,
                    ResourceManager.getMessage("not.exists", Comments.get("Department")));
        }
        return result;
    }

    @Override
    public long getCount() throws SQLException {
        return deptDao.getCount();
    }

    @Override
    public long getCount(Condition condition) throws SQLException {
        return deptDao.getCount(condition);
    }

}
