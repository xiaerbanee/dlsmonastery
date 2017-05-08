package net.myspring.cloud.modules.input.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.K3CloudActionEnum;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.util.json.ObjectMapperUtils;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuj on 2017/5/8.
 */
@Component
public class K3cloudManager {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, OkHttpClient> okHttpClientMap = Maps.newHashMap();


    public Boolean login() {
        AccountKingdeeBook accountKingdeeBook = new AccountKingdeeBook();
        KingdeeBook kingdeeBook = new KingdeeBook();
        List<Object> list = Lists.newArrayList();
        list.add(kingdeeBook.getKingdeeDbid());
        list.add(accountKingdeeBook.getUsername());
        list.add(accountKingdeeBook.getPassword());
        list.add("2052");
        OkHttpClient okHttpClient = getClient(SecurityUtils.getAccountId());
        Request request = new Request.Builder()
                .url(kingdeeBook.getKingdeeUrl() + K3CloudActionEnum.VALIDATE_USER.getValue())
                .addHeader("content-type", "application/json;charset:utf-8")
                .put(getRequestBody(list))
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            response.body().close();
            return result.contains("\"LoginResultType\":1");
        } catch (IOException e) {
            return false;
        }
    }

    public  String invoke(KingdeeBook kingdeeBook,String action, String formId, String content) {
        List<Object> list = Lists.newArrayList();
        list.add(formId);
        list.add(content);
        Request request = new Request.Builder()
                .url(kingdeeBook.getKingdeeUrl() + action)
                .addHeader("content-type", "application/json;charset:utf-8")
                .put(getRequestBody(list))
                .build();
        OkHttpClient okHttpClient = getClient(SecurityUtils.getAccountId());
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            response.body().close();
            return result;
        } catch (IOException e) {
            return "";
        }
    }

    private RequestBody getRequestBody(List<Object> paramList) {
        String paramStr = ObjectMapperUtils.writeValueAsString(paramList);
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("format", 1);
        paramMap.put("useragent", "ApiClient");
        paramMap.put("rid", UUID.randomUUID().toString().hashCode());
        paramMap.put("parameters", paramStr);
        paramMap.put("timestamp", new Date().toString());
        paramMap.put("v", "1.0");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),ObjectMapperUtils.writeValueAsString(paramMap));
        return requestBody;
    }

    private OkHttpClient getClient(String accountId) {
        if (!okHttpClientMap.containsKey(accountId)) {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS).readTimeout(10000, TimeUnit.MILLISECONDS).writeTimeout(10000, TimeUnit.MILLISECONDS).cookieJar(new CookieJar() {
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
                    return cookies != null ? cookies : Lists.newArrayList();
                }
            }).build();
            okHttpClientMap.put(accountId, client);
        }
        return okHttpClientMap.get(accountId);
    }

}
