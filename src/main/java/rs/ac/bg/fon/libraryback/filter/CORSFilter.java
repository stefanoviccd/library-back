package rs.ac.bg.fon.libraryback.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class CORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)  servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        System.out.println("Cors filter check..");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        System.out.println("First filter check passed..");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        System.out.println("Second filter check passed..");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        System.out.println("Third filter check passed..");
        response.setHeader("Access-Control-Max-Age", "3600");
        System.out.println("4th filter check passed..");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        System.out.println("5th filter check passed..");
        filterChain.doFilter(request, response);
        System.out.println("Final filter check passed..");



    }
}
