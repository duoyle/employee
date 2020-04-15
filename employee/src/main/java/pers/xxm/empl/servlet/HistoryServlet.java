package pers.xxm.empl.servlet;

import lombok.Getter;
import org.apache.http.HttpStatus;
import pers.xxm.empl.common.Constant;
import pers.xxm.empl.service.HistoryService;
import pers.xxm.empl.service.Impl.HistoryServiceImpl;
import pers.xxm.empl.service.factory.ServiceFactory;
import pers.xxm.empl.vo.History;
import pers.xxm.empl.vo.Page;
import pers.xxm.resource.Comments;
import pers.xxm.resource.ResourceManager;
import pers.xxm.trouble.BusinessException;
import pers.xxm.trouble.ErrorCode;

import javax.servlet.annotation.WebServlet;
import java.util.HashMap;

// 名称很关键，不要重复，否则导致无效
@WebServlet(name = "HistoryServlet", urlPatterns = {Constant.SERVLET_PATH + "/history/*"})
public class HistoryServlet extends DispatcherServlet {

    @Override
    protected String getTitle() {
        return Comments.get("Employee"); // print/format等会自动调用toString
    }

    @Override
    protected String getValidateParams() {
        return null;
    }

    @Getter
    private Page<History> page;

    private HistoryService service; // 服务层实例

    @Override
    public void init() {
        super.init();
        setSearchColumns();
        setService();
    }

    // 用于取得列表的ajax请求
    public void list() throws Exception {
        this.page = new Page<>();
        listInPage(this.service, this.page);
    }

    @Override
    public String locate() {
        String pathFlag = this.request.getPathInfo();
        switch (pathFlag) {
            case "/list":
                this.request.setAttribute("searchColumns", this.getSearchColumns());
                break;
            default:
                throw new BusinessException(HttpStatus.SC_NOT_FOUND, ResourceManager.getMessage("not.found"));
        }
        return this.getForwardPath();
    }

    // 私有方法
    // 取得业务层实例
    private void setService() {
        try {
            this.service = ServiceFactory.getFactory().getObject(HistoryServiceImpl.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new BusinessException(ErrorCode.UNKNOWN, ResourceManager.getMessage("unknown"));
        }
    }
    // 设置查询依照的列
    private void setSearchColumns() {
        this.searchColumns = new HashMap<>();
        this.searchColumns.put("Employee.empNo", "员工编号");
        this.searchColumns.put("Employee.empName", "员工姓名");
    }
}
