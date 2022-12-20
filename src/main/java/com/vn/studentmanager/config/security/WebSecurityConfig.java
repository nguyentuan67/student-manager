package com.vn.studentmanager.config.security;

import com.vn.studentmanager.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    DataSource dataSource;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AppAuthenticationSuccessHandler successHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(this.passwordEncoder);
    }
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/resources/**", "/static/**", "/repository/**", "/assets/**",
                "/fonts/**", "/vpp/**", "/vvp/**", "/smsws**","/smsws/**", "/v1/**", "/momt/send_mt", "/public/login.json", "/api-public/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/webjars/**", "/resources/**", "/assets/**", "/fonts/**", "/ws/**", "/static/**").permitAll()
                .antMatchers("/student/detail", "/student/result", "/classroom/detail").hasAnyAuthority("USER", "TEACHER")
                .antMatchers("/teacher/detail", "/classroom/", "/classroom/**/detail").hasAnyAuthority("TEACHER")
                .antMatchers("/user/**", "/student/**", "/classroom/**", "/teacher/**", "/post/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                    .exceptionHandling()
                        .accessDeniedPage("/error/403.html?authorization_error=true")
                        .and()
                        .headers().frameOptions().disable().and()
                        .formLogin()
                        .usernameParameter("j_username")
                        .passwordParameter("j_password")
                        .loginPage("/login.html")
                        .failureUrl("/login.html?authentication_error=true")
                        .loginProcessingUrl("/login.html")
                        .successHandler(successHandler)
                        .permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/login.html")
                    .logoutUrl("/logout.html")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout.html")) // for POST and GET
                    .deleteCookies( "JSESSIONID" )
                    .invalidateHttpSession(true)
                    .permitAll();
    }
}
