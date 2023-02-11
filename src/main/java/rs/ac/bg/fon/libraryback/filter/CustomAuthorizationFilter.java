package rs.ac.bg.fon.libraryback.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.ac.bg.fon.libraryback.model.User;
import rs.ac.bg.fon.libraryback.service.IUserService;
import rs.ac.bg.fon.libraryback.utility.JsonWebTokenUtility;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private JsonWebTokenUtility jsonWebTokenUtility=new JsonWebTokenUtility();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if(request.getServletPath().equals("api/v1/login") || request.getServletPath().equals("/api/v1/refreshToken")){
        filterChain.doFilter(request, response);
    }
    else {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String username= jsonWebTokenUtility.extractUsernameFromHeader(authorizationHeader);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jsonWebTokenUtility.getUsernamePasswordAuthenticationToken(authorizationHeader,username);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request, response);
            } catch (Exception ex) {
                log.error("Error authorisins: {}", ex.getMessage());
                response.setHeader("error", ex.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> errors = new HashMap<>();
                errors.put("error_message", ex.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }


        } else {
            filterChain.doFilter(request, response);
        }

    }
    }


}
