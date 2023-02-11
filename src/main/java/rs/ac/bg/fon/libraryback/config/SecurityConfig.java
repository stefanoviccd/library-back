package rs.ac.bg.fon.libraryback.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import rs.ac.bg.fon.libraryback.filter.CustomAuthenticationFilter;
import rs.ac.bg.fon.libraryback.filter.CustomAuthorizationFilter;
import rs.ac.bg.fon.libraryback.filter.CustomLogoutSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.cors().and().csrf().disable();
        http.authorizeRequests().antMatchers("/swagger-ui/","/api/login/**","/api/v1/register/**", "/api/v2/register/**", "/api/v1/refreshToken/**" ).permitAll()
        .antMatchers("api/v1/user/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                        .and()
                .logout()
                .logoutUrl("api/v1/logout")
                .logoutSuccessUrl("/api/v1/logoutSuccess")
                .logoutSuccessHandler(logoutSuccessHandler())
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().authenticationEntryPoint(swaggerAuthenticationEntryPoint());

        http.addFilter(new CustomAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

    }
    @Override
    public void configure(final WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers(
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/v2/api-docs",
                "/webjars/**");
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }


    @Bean
    public BasicAuthenticationEntryPoint swaggerAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Swagger Realm");
        return entryPoint;
    }


}
