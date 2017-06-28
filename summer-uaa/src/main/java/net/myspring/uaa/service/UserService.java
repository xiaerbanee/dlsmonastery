package net.myspring.uaa.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.AccountWeixinDto;
import net.myspring.uaa.dto.PermissionDto;
import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.uaa.manager.WeixinManager;
import net.myspring.uaa.repository.AccountPermissionRepository;
import net.myspring.uaa.repository.AccountWeixinDtoRepository;
import net.myspring.uaa.repository.PermissionRepository;
import net.myspring.util.collection.CollectionUtil;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private WeixinManager weixinManager;
    @Autowired
    private AccountWeixinDtoRepository accountWeixinDtoRepository;

    public Map<String, Object> login(String username, String password, String weixinCode, String accountId) {
        String url = "http://localhost:1200/login";
        Map<String, Object> map = Maps.newHashMap();
        Boolean success = true;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).readTimeout(3, TimeUnit.SECONDS).cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                Map<String, Cookie> cookieMap = Maps.newHashMap();
                if (cookieStore.containsKey(url.host())) {
                    for (Cookie cookie : cookieStore.get(url.host())) {
                        cookieMap.put(cookie.name(), cookie);
                    }
                }
                for (Cookie cookie : cookies) {
                    cookieMap.put(cookie.name(), cookie);
                }
                cookieStore.put(url.host(), Lists.newArrayList(cookieMap.values()));
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<>();
            }
        }).build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            success = false;
        }
        response.body().close();
        request = new Request.Builder()
                .url(response.priorResponse().request().url())
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            success = false;
        }
        response.body().close();

        FormBody.Builder builder = new FormBody.Builder();
        if (StringUtils.isNotBlank(weixinCode)) {
            builder.add("weixinCode", weixinCode);
        }
        if (StringUtils.isNotBlank(password)) {
            builder.add("password", password);
        }
        if (StringUtils.isNotBlank(username)) {
            builder.add("username", username);
        }
        if (StringUtils.isNotBlank(accountId)) {
            builder.add("accountId", accountId);
        }
        RequestBody body = builder.build();

        HttpUrl requestUrl = response.request().url();
        request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            success = false;
        }
        response.body().close();
        String sessionId = "";
        if (requestUrl.toString().equals(response.priorResponse().request().url().toString())) {
            success = false;
        } else {
            request = new Request.Builder()
                    .url(response.priorResponse().request().url())
                    .build();
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                success = false;
            }
            response.body().close();
            List<Cookie> cookieList = client.cookieJar().loadForRequest(request.url());
            for (Cookie cookie : cookieList) {
                if ("JSESSIONID".equals(cookie.name())) {
                    sessionId = cookie.value();
                }
            }
        }
        map.put("success", success);
        map.put("JSESSIONID", sessionId);
        return map;
    }

    public List<AccountWeixinDto> getWeixinAccountList(String weixinCode) {
        List<AccountWeixinDto> accountWeixinList = Lists.newArrayList();
        WeixinSessionDto weixinSession = weixinManager.findWeixinSessionDto(weixinCode);
        String openId = weixinSession.getOpenid();
        if (StringUtils.isNotBlank(openId)) {
            accountWeixinList = accountWeixinDtoRepository.findByOpenId(openId);
        }
        return accountWeixinList;
    }
}

