package pers.xxm.empl.servlet;

import lombok.Getter;
import pers.xxm.bean.BeanUtil;
import pers.xxm.empl.common.Constant;
import pers.xxm.empl.service.DepartmentService;
import pers.xxm.empl.service.Impl.DepartmentServiceImpl;
import pers.xxm.empl.service.factory.ServiceFactory;
import pers.xxm.empl.vo.Department;
import pers.xxm.empl.vo.Page;
import pers.xxm.resource.Comments;
import pers.xxm.resource.ResourceManager;
import pers.xxm.trouble.BusinessException;

import javax.servlet.annotation.WebServlet;
import java.util.HashMap;

/**
 * Created by XuXuemin on 20/3/3
 */
@WebServlet(name = "DepartmentServlet", urlPatterns = {Constant.SERVLET_PATH + "/dept/*"})
public class DepartmentServlet extends DispatcherServlet {
    @Getter
    private Department dept;
    @Getter
    private Page<Department> page;

    private DepartmentService service; // 服务层实例

    @Override
    public void init() {
        super.init();
        setSearchColumns();
        setService();
    }

    @Override
    public String getTitle() {
        return Comments.get("Department"); // print/format等会自动调用toString
    }

    public void insert() throws Exception {
        this.dept = new Department();
        this.validSetBean(this.dept); // 验证和设置属性
        this.service.insert(this.dept); // 调用服务层插入数据
        this.writeResponse(ResourceManager.getMessage("insert.success", getTitle())); // 回应结果
    }

    public void update() throws Exception {
        this.dept = new Department();
        this.validSetBean(this.dept);
        this.service.update(this.dept);
        this.writeResponse(ResourceManager.getMessage("update.success", getTitle())); // 回应结果
    }

    public void delete() throws Exception {
        String deptNo = getParamValue("deptNo", String.class);
        this.service.delete(deptNo);
        this.writeResponse(ResourceManager.getMessage("delete.success", getTitle())); // 回应结果
    }

    // 列表功能，search也走这里
    public void list() throws Exception {
        this.page = new Page<>();
        listInPage(this.service, this.page);
    }

    @Override
    public String locate() throws Exception {
        String pathFlag = this.request.getPathInfo();
        switch (pathFlag) {
            case "/insert":
                break;
            case "/update":
                this.dept = new Department();
                this.validSetBean(this.dept, "deptNo");
                this.dept = this.service.getByKey(this.dept.getDeptNo());
                BeanUtil.setRequest(request, this.dept);
                break;
            case "/list":
            case "/select":
                this.request.setAttribute("searchColumns", this.getSearchColumns());
                break;
        }
        return this.getForwardPath();
    }

    // 默认验证的字段
    @Override
    public String getValidateParams() {
        return "deptNo|deptName";
    }

    // 私有方法
    // 取得业务层实例
    private void setService() {
        try {
            this.service = ServiceFactory.getFactory().getObject(DepartmentServiceImpl.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new BusinessException();
        }
    }
    // 设置查询依照的列
    private void setSearchColumns() {
        this.searchColumns = new HashMap<>();
        this.searchColumns.put("Department.deptName", "部门名称");
        this.searchColumns.put("Department.deptNo", "部门编号");
    }
}
