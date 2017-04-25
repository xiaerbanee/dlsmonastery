package net.myspring.cloud.common.annotation;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("doubleSubmit",false);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Token annotation = method.getAnnotation(Token. class );
            if (annotation != null ) {
                boolean needSaveSession = annotation.save();
                HttpSession httpSession = request.getSession(false);
                Set<String> tokenSet = null;
                if(httpSession.getAttribute("tokenSet")==null) {
                    tokenSet = Sets.newHashSet();
                } else {
                    tokenSet = (Set<String>) httpSession.getAttribute("tokenSet");
                }
                if (needSaveSession) {
                    String token = UUID.randomUUID().toString();
                    httpSession.setAttribute("token", token);
                    request.setAttribute("token", token);
                    tokenSet.add(token);
                }
                boolean needRemoveSession = annotation.remove();
                if (needRemoveSession) {
                    String clientToken = request.getParameter("token");
                    if(StringUtils.isNotBlank(clientToken) && tokenSet.contains(clientToken)) {
                        tokenSet.remove(clientToken);
                    } else {
                        httpSession.setAttribute("tokenSet", tokenSet);
                        request.setAttribute("doubleSubmit",true);
                    }
                }
                httpSession.setAttribute("tokenSet", tokenSet);
            }
        }
        return super.preHandle(request, response, handler);
    }
}

