package net.myspring.gateway.fiter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * Created by liuj on 2017/3/27.
 */
public class AccessFilter extends ZuulFilter {
    private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if("/api/uaa/oauth/token".equals(ctx.getRequest().getRequestURI())) {
            return true;
        } else {
            if(StringUtils.isBlank(ctx.getRequest().getHeader("Authorization"))) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if("/api/uaa/oauth/token".equals(ctx.getRequest().getRequestURI())) {
            ctx.addZuulRequestHeader("Authorization","Basic " + Base64.getEncoder().encodeToString("web_app:web_app".getBytes()));
        } else {
            Cookie[] cookies = ctx.getRequest().getCookies();
            if(cookies.length>0) {
                for(int i=0;i<cookies.length;i++) {
                    Cookie cookie = cookies[i];
                    if("Authorization".equals(cookie.getName())) {
                        ctx.addZuulRequestHeader("Authorization","bearer " + cookie.getValue());
                        break;
                    }
                }
            }
        }
        return null;
    }
}
