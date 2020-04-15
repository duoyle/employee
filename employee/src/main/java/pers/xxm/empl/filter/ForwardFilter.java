package pers.xxm.empl.filter;

import org.apache.http.HttpStatus;
import pers.xxm.empl.common.Constant;
import pers.xxm.resource.ResourceManager;
import pers.xxm.trouble.BusinessException;
import pers.xxm.trouble.ErrorCode;
import pers.xxm.trouble.ExceptionHandler;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by XuXuemin on 20/3/19
 */
// 这里的过滤请求默认是REQUEST，也就是只拦截客户端请求，还有FORWARD,INCLUDE,ERROR等
// dispatcherTypes = { DispatcherType.REQUEST }，initParams多个参数末尾加逗号
@WebFilter(filterName = "ForwardFilter", urlPatterns = {"/*"}, initParams = {
        @WebInitParam(name = "passSuffix", value = ".html,.htm") // 标示静态网页的后缀
})
// 多个Filter执行顺序：Filter1->Filter2->程序->Filter2->Filter1，chain.doFilter之后跳转到下个，后面的代码执行程序后回调。
// 多个过滤器的顺序按照web.xml中的配置先后，但是注解的方式按照过滤器名称的排序，A开头在B开头之前执行。拦截过滤器最好设最后一个。
public class ForwardFilter implements Filter {
    private String[] passSuffixes;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        // path等于req.getServletPath() + req.getPathInfo()（结果可能包含null，因为pathInfo可能为null，其他若空则为""）
        // http://localhost:8080访问主页时req.getRequestURI()为contextPath
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 主页不做servlet控制，因为主页自动跳转到index.jsp/index.html，具体在其内控制
        if (isExcluded(path)) {
            chain.doFilter(request, response);
        } else if (isHtml(path)) {
            path = Constant.PAGE_PATH + path;
            req.getRequestDispatcher(path).forward(request, response);
        } else { // 普通访问地址
            // 后面不执行其他request过滤器了，如果还有其他forward过滤器则执行
            path = Constant.SERVLET_PATH + path;
            req.getRequestDispatcher(path).forward(request, response);
        }
    }

    @Override
    public void init(FilterConfig config) { // 父类的方法throws异常这里可以没有，但若有父类也一定有
        String passSuffix = config.getInitParameter("passSuffix");
        this.passSuffixes = passSuffix.split(",");
    }

    // 是否是html文件
    private boolean isExcluded(String path) {
        // 首先判断是主页或者访问的资源
        return path.startsWith(Constant.RESOURCE_PREFIX)
                || path.equals("/") || path.equals("/favicon.ico");
    }

    // 是否是静态网页
    private boolean isHtml(String path) {
        for (String suffix : passSuffixes) {
            if (path.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
