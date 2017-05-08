package net.myspring.cloud.modules.input.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuj on 2017/5/8.
 */
@Component
public class K3cloudManager {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, OkHttpClient> okHttpClientMap = Maps.newHashMap();

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
