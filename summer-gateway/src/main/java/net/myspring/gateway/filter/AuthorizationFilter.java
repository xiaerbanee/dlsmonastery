package net.myspring.gateway.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liuj on 2017/3/27.
 */
public class AuthorizationFilter extends ZuulFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    private ObjectMapper mapper = new ObjectMapper();

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
//        if(!"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
//            return true;
//        }
        return false;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        LinkedHashMap<String,Object> principal = Maps.newLinkedHashMap();
        if(auth !=  null) {
            principal= (LinkedHashMap) ((LinkedHashMap)auth.getUserAuthentication().getDetails()).get("principal");
        }
        String accountId = (String) principal.get("accountId");
        String companyName = (String) principal.get("companyName");
        String key = "authorityCache:" + companyName+accountId;
        Map<String,String> map = (Map<String, String>) redisTemplate.opsForValue().get(key);
        String permissionKey=ctx.getRequest().getRequestURI()+ctx.getRequest().getMethod();
        if(map.containsKey(permissionKey)){
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
            return null;
        }else{
            ctx.setSendZuulResponse(false);
            try {
                ctx.getResponse().sendError(401,"无权限");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
