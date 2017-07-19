package net.myspring.uaa.config;

import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.uaa.datasource.DbContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liuj on 2016-08-20.
 */
public class MyHandlerInterceptor implements HandlerInterceptor {
    @Value("companyNames")
    private String[] companyNames;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List<String> companyNameList = Arrays.asList(companyNames);
        String companyName = request.getParameter("companyName");
        if(StringUtils.isBlank(companyName)) {
            if(companyNameList.contains(CompanyNameEnum.JXOPPO.name())) {
                companyName = CompanyNameEnum.JXOPPO.name();
            } else {
                companyName = CompanyNameEnum.IDVIVO.name();
            }
        }
        DbContextHolder.get().setCompanyName(companyName);
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