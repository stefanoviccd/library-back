package rs.ac.bg.fon.libraryback.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.ac.bg.fon.libraryback.service.impl.UserDetailsServiceImpl;
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
    private UserDetailsServiceImpl userDetailsService;


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
                    .loadUserByUsername(jwtUtility.getUsernameFromToken(token));

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


    }


    }


