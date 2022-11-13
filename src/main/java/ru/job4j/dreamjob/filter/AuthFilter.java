package ru.job4j.dreamjob.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class AuthFilter implements Filter {
    private static final String LOGIN_PAGE = "loginPage";
    private static final String LOGIN = "login";
    private static final String FORM_ADD_USER = "formAddUser";
    private static final String REGISTRATION = "registration";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        Set<String> filterValues = new HashSet<>(Arrays.asList(
                LOGIN_PAGE,
                LOGIN,
                FORM_ADD_USER,
                REGISTRATION));
        if (filter(uri, filterValues)) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/loginPage");
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean filter(String uri, Collection<String> values) {
        return values.stream().anyMatch(uri::endsWith);
    }
}