package pers.xxm.empl.service;

import pers.xxm.condition.Condition;
import pers.xxm.empl.vo.History;

import java.sql.SQLException;
import java.util.List;

/**
 * Created By XuXuemin
 */
public interface HistoryService extends IService<Integer, History> {
    /**
     * 按条件查询
     * @param condition where后的匹配条件
     * @return 匹配结果集
     * @throws SQLException SQL异常
     */
    List<History> search(Condition condition) throws SQLException;

}
