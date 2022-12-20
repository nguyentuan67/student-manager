package com.vn.studentmanager.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(AppAuthenticationSuccessHandler.class);
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1,
                                        Authentication authentication) throws IOException, ServletException {

        boolean hasUserRole = false;
        boolean hasAdminRole = false;
        boolean hasTeacherRole = false;
        try {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                if (grantedAuthority.getAuthority().equals("USER")) {
                    hasUserRole = true;
                    break;
                } else if (grantedAuthority.getAuthority().equals("TEACHER")) {
                    hasTeacherRole = true;
                    break;
                } else if (grantedAuthority.getAuthority().equals("ADMIN")) {
                    hasAdminRole = true;
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        if (hasUserRole) {
            redirectStrategy.sendRedirect(arg0, arg1, "/");
        } else if (hasTeacherRole) {
            redirectStrategy.sendRedirect(arg0, arg1, "/");
        } else if (hasAdminRole) {
            redirectStrategy.sendRedirect(arg0, arg1, "/user/list");
        } else {
            throw new IllegalStateException();
        }
    }
}
