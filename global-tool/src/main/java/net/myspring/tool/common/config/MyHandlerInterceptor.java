package net.myspring.tool.common.config;

import net.myspring.FutureApplication;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.utils.ThreadLocalContext;
import net.myspring.future.modules.hr.domain.Account;
import net.myspring.future.modules.hr.service.AccountService;
import net.myspring.future.modules.sys.security.CustomUserDetails;
import net.myspring.tool.common.dataSource.DbContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liuj on 2016-08-20.
 */
public class MyHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        DbContextHolder.get().remove();
    }
}