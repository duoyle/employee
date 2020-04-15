package pers.xxm.empl.dao;

import pers.xxm.condition.Condition;
import pers.xxm.empl.vo.History;

import java.sql.SQLException;
import java.util.List;

public interface HistoryDao extends IDao<Integer, History> {
    /**
     * 按条件查询
     * @param condition where后的匹配条件
     * @return 匹配结果集
     * @throws SQLException SQL异常
     */
    List<History> search(Condition condition) throws SQLException;

    /**
     * 删除对应雇员的历史记录
     * @param empNo 员工编号
     * @throws SQLException SQL异常
     */
    void deleteByEmpl(String empNo) throws SQLException;

}
