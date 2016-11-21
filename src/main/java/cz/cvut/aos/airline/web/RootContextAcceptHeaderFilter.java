package cz.cvut.aos.airline.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jakubchalupa
 * @since 21.11.16
 */
public class RootContextAcceptHeaderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //empty
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String accept = httpServletRequest.getHeader("Accept");
        String contextPath = httpServletRequest.getContextPath();

        if(("".equals(contextPath) || "/".equals(contextPath)) && ("application/json".equals(accept) || "application/xml".equals(accept))) {
            httpServletResponse.sendRedirect("/app");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        //empty
    }

}
