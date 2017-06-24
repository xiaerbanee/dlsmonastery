package net.myspring.gateway.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuj on 2017/3/27.
 */
public class LoginFilter extends ZuulFilter {
    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if("/user/login".equals(ctx.getRequest().getRequestURI())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String responseData = "";
        try {
            responseData = CharStreams.toString(new InputStreamReader(ctx.getResponseDataStream(), "UTF-8"));
            ctx.setResponseBody(responseData);
        } catch (IOException e) {
        }
        if(StringUtils.isNotBlank(responseData)) {
            try {
                Map<String,Object> map = mapper.readValue(responseData, new TypeReference<HashMap<String,String>>(){});
                Cookie cookie =  new Cookie("JSESSIONID", (String) map.get("JSESSIONID"));
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                ctx.getResponse().addCookie(cookie);
            } catch (IOException e) {
            }
        }
        return null;
    }
}
