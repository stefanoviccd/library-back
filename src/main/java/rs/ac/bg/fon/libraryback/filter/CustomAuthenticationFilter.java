package rs.ac.bg.fon.libraryback.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.utility.JsonWebTokenUtility;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private JsonWebTokenUtility jsonWebTokenUtility= new JsonWebTokenUtility();


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String passport = request.getParameter("password");
        log.info("Username is {}, and passport is {}", username, passport);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, passport);
        return authenticationManager.authenticate(token);


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user = (User) authentication.getPrincipal();
        String accessToken = jsonWebTokenUtility.generateToken(user,request.getRequestURL().toString(), 10*60*1000);
        String refreshToken = jsonWebTokenUtility.generateToken(user,request.getRequestURL().toString(), 30*60*1000);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);


    }
}
