package rs.ac.bg.fon.libraryback.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.ac.bg.fon.libraryback.service.CustomUserDetailsService;
import rs.ac.bg.fon.libraryback.utility.JWTUtility;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter implements Filter {
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String token = request.getHeader("Authorization");
            System.out.println("JWTFilter: Authorization: " + token);
        if (token==null || token.equals("null")) {
            filterChain.doFilter(request, response);
            return;
        }
        if(!request.getServletPath().equals("/api/v1/user/login")){
            // Get user identity and set it on the spring security context
            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(jwtUtility.extractUsername(token));

            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails == null ?
                            List.of() : userDetails.getAuthorities()
            );
            if (!jwtUtility.validateToken(token, userDetails)) {
                filterChain.doFilter(request, response);
                return;
            }

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
        else{
           // filterChain.doFilter(request, response);
        }

    }


              /*  userName = jwtUtility.extractUsername(token);
                System.out.println("JWTFilter: username: " + userName);
                if (null != userName) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    if (jwtUtility.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        if (SecurityContextHolder.getContext().getAuthentication() == null)
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        throw new ServletException("Authorization token is not valid!");
                    }

                } else {
                    throw new ServletException("Authorization token is not valid!");
                }
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

                filterChain.doFilter(request, response);
            */

    }


