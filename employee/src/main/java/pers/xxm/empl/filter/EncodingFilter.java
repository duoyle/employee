package pers.xxm.empl.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"}, initParams =
        {@WebInitParam(name = "encoding", value = "UTF-8")})
public class EncodingFilter implements Filter {
    private String encoding;

    @Override
    public void destroy() {
        this.encoding = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(this.encoding);
        response.setCharacterEncoding(encoding);
        chain.doFilter(request, response); // 这样走容器默认的Servlet，也就是按照正常配置走
        // 这里可用request.getRequestDispatcher("/pages/").forward(request, response)将请求重定向代替默认过滤
    }

    @Override
    public void init(FilterConfig config) { // 父类的方法throws异常这里可以没有，但若有父类也一定有
        this.encoding = config.getInitParameter("encoding");
    }

}
