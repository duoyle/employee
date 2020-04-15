package pers.xxm.empl.servlet;

import lombok.Getter;
import org.apache.http.HttpStatus;
import pers.xxm.empl.common.Constant;
import pers.xxm.empl.service.EmployeeService;
import pers.xxm.empl.service.Impl.EmployeeServiceImpl;
import pers.xxm.empl.service.factory.ServiceFactory;
import pers.xxm.empl.vo.Employee;
import pers.xxm.empl.vo.Page;
import pers.xxm.resource.Comments;
import pers.xxm.resource.ResourceManager;
import pers.xxm.trouble.BusinessException;
import pers.xxm.trouble.ErrorCode;

import javax.servlet.annotation.WebServlet;
import java.util.HashMap;

/**
 * Created by XuXuemin on 20/3/3
 */
@WebServlet(name = "EmployeeServlet", urlPatterns = {Constant.SERVLET_PATH + "/employee/*"})
public class EmployeeServlet extends DispatcherServlet {
    @Getter
    private Employee empl;
    @Getter
    private Page<Employee> page;

    private EmployeeService service; // 服务层实例

    @Override
    public void init() {
        super.init();
        setSearchColumns();
        setService();
    }

    @Override
    public String getTitle() {
        return Comments.get("Employee"); // print/format等会自动调用toString
    }

    public void insert() throws Exception {
        this.empl = new Employee();
        this.validSetBean(this.empl); // 验证和设置属性
        this.service.insert(this.empl); // 调用服务层插入数据
        this.writeResponse(ResourceManager.getMessage("insert.success", getTitle())); // 回应结果
    }

    public void update() throws Exception {
        this.empl = new Employee();
        this.validSetBean(this.empl);
        this.service.update(this.empl);
        this.writeResponse(ResourceManager.getMessage("update.success", getTitle())); // 回应结果
    }

    public void delete() throws Exception {
        String empNo = getParamValue("empNo", String.class);
        this.service.delete(empNo);
        this.writeResponse(ResourceManager.getMessage("delete.success", getTitle())); // 回应结果
    }

    public void leave() throws Exception {
        String empNo = getParamValue("empNo", String.class);
        this.service.leave(empNo);
        this.writeResponse(ResourceManager.getMessage("leave.success", getTitle())); // 回应结果
    }

    // 用于取得列表的ajax请求
    public void list() throws Exception {
        this.page = new Page<>();
        listInPage(this.service, this.page);
    }

    // 用于取得一条数据的Ajax请求
    public void get() throws Exception {
        this.empl = new Employee();
        this.validSetBean(this.empl, "empNo");
        this.empl = this.service.getByKey(this.empl.getEmpNo());
        this.writeResponse(this.empl); // 写入到回应数据
    }

    @Override
    public String locate() {
        String pathFlag = this.request.getPathInfo();
        switch (pathFlag) {
            case "/insert":
            case "/update":
                break;
            case "/list":
            case "/select":
                this.request.setAttribute("searchColumns", this.getSearchColumns());
                break;
            default:
                throw new BusinessException(HttpStatus.SC_NOT_FOUND, ResourceManager.getMessage("not.found"));
        }
        return this.getForwardPath();
    }

    // 默认验证的字段
    @Override
    public String getValidateParams() {
        return "empNo|dept.deptNo|empName|empSex|entryDate|salary";
    }

    // 私有方法
    // 取得业务层实例
    private void setService() {
        try {
            this.service = ServiceFactory.getFactory().getObject(EmployeeServiceImpl.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new BusinessException(ErrorCode.UNKNOWN, ResourceManager.getMessage("unknown"));
        }
    }
    // 设置查询依照的列
    private void setSearchColumns() {
        this.searchColumns = new HashMap<>();
        this.searchColumns.put("Department.deptName", "部门名称");
        this.searchColumns.put("Department.deptNo", "部门编号");
        this.searchColumns.put("Employee.empNo", "员工编号");
        this.searchColumns.put("Employee.empName", "员工姓名");
    }

}
