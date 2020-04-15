package pers.xxm.empl.service.Impl;

import pers.xxm.condition.Condition;
import pers.xxm.empl.dao.HistoryDao;
import pers.xxm.empl.dao.factory.DaoFactory;
import pers.xxm.empl.dao.impl.HistoryDaoImpl;
import pers.xxm.empl.service.HistoryService;
import pers.xxm.empl.service.IService;
import pers.xxm.empl.vo.History;
import pers.xxm.empl.vo.Page;

import java.sql.SQLException;
import java.util.List;

public class HistoryServiceImpl implements HistoryService {
    private HistoryDao histDao;

    public HistoryServiceImpl() throws InstantiationException, IllegalAccessException {
        histDao = DaoFactory.getFactory().getObject(HistoryDaoImpl.class);
    }


    @Override
    public List<History> search(Condition condition) throws SQLException {
        return histDao.search(condition);
    }

    @Override
    public void delete(Integer key) throws SQLException {
        histDao.delete(key);
    }

    @Override
    public void listInPage(final Page<History> page) throws SQLException {
        IService.listInPage(this.histDao, page);
    }

    @Override
    public List<History> listAll() throws SQLException {
        return histDao.findAll();
    }

    @Override
    public History getByKey(Integer key) throws SQLException {
        return histDao.getByKey(key);
    }

    @Override
    public long getCount() throws SQLException {
        return histDao.getCount();
    }

    @Override
    public long getCount(Condition condition) throws SQLException {
        return histDao.getCount(condition);
    }

}
