package net.myspring.cloud.modules.input.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.K3CloudActionEnum;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.input.dto.K3CloudSaveDto;
import net.myspring.cloud.modules.input.dto.K3CloudSaveExtendDto;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.BoolEnum;
import net.myspring.util.json.ObjectMapperUtils;
import net.sf.json.JSONObject;
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


    public Boolean login(String url,String dbid,String username,String password) {
        List<Object> list = Lists.newArrayList();
        list.add(dbid);
        list.add(username);
        list.add(password);
        list.add("2052");
        OkHttpClient okHttpClient = getClient(SecurityUtils.getAccountId());
        Request request = new Request.Builder()
                .url(url + K3CloudActionEnum.VALIDATE_USER.getValue())
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

    private String invoke(String url,String action, String formId, String content) {
        List<Object> list = Lists.newArrayList();
        list.add(formId);
        list.add(content);
        Request request = new Request.Builder()
                .url(url + action)
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

    public K3CloudSaveDto save(K3CloudSaveDto k3CloudSaveDto) {
        KingdeeBook kingdeeBook = k3CloudSaveDto.getKingdeeBook();
        String result = invoke(kingdeeBook.getKingdeeUrl(),K3CloudActionEnum.SAVE.getValue(),k3CloudSaveDto.getFormId(),k3CloudSaveDto.getContent());
        JSONObject jsonObject = JSONObject.fromObject(result);
        if (BoolEnum.TRUE.getValue().equals(jsonObject.getJSONObject("Result").getJSONObject("ResponseStatus").getString("IsSuccess"))) {
            String billNo = jsonObject.getJSONObject("Result").getString("Number");
            Map<String, Object> root = Maps.newLinkedHashMap();
            root.put("CreateOrgId", 0);
            root.put("Numbers", billNo);
            String content = ObjectMapperUtils.writeValueAsString(root);
            invoke(kingdeeBook.getKingdeeUrl(),K3CloudActionEnum.SUBMIT.getValue(),k3CloudSaveDto.getFormId(), content);
            if (k3CloudSaveDto.getAutoAudit()) {
                invoke(kingdeeBook.getKingdeeUrl(),K3CloudActionEnum.AUDIT.getValue(),k3CloudSaveDto.getFormId(), content);
            }
            k3CloudSaveDto.setBillNo(billNo);
            k3CloudSaveDto.setSuccess(true);
        } else {
            k3CloudSaveDto.setSuccess(false);
        }
        return k3CloudSaveDto;
    }


    public K3CloudSaveExtendDto save(K3CloudSaveExtendDto k3CloudSaveExtendDto) {
        KingdeeBook kingdeeBook = k3CloudSaveExtendDto.getKingdeeBook();
        String result = invoke(kingdeeBook.getKingdeeUrl(),K3CloudActionEnum.SAVE.getValue(),k3CloudSaveExtendDto.getFormId(),k3CloudSaveExtendDto.getContent());
        JSONObject jsonObject = JSONObject.fromObject(result);
        if (BoolEnum.TRUE.getValue().toString().equals(jsonObject.getJSONObject("Result").getJSONObject("ResponseStatus").getString("IsSuccess"))) {
            String billNo = jsonObject.getJSONObject("Result").getString("Number");
            Map<String, Object> root = Maps.newLinkedHashMap();
            root.put("CreateOrgId", 0);
            root.put("Numbers", billNo);
            String content = ObjectMapperUtils.writeValueAsString(root);
            invoke(kingdeeBook.getKingdeeUrl(),K3CloudActionEnum.SUBMIT.getValue(),k3CloudSaveExtendDto.getFormId(), content);
            invoke(kingdeeBook.getKingdeeUrl(),K3CloudActionEnum.AUDIT.getValue(),k3CloudSaveExtendDto.getFormId(), content);
            k3CloudSaveExtendDto.setBillNo(billNo);
            String nextBillNo = k3CloudSaveExtendDto.getNextBillNo();
            root = Maps.newLinkedHashMap();
            root.put("CreateOrgId", 0);
            root.put("Numbers", nextBillNo);
            k3CloudSaveExtendDto.setBillNo(k3CloudSaveExtendDto.getBillNo() + CharConstant.COMMA + nextBillNo);
            content = ObjectMapperUtils.writeValueAsString(root);
            invoke(kingdeeBook.getKingdeeUrl(),K3CloudActionEnum.SUBMIT.getValue(),k3CloudSaveExtendDto.getNextFormId(), content);
            invoke(kingdeeBook.getKingdeeUrl(),K3CloudActionEnum.AUDIT.getValue(),k3CloudSaveExtendDto.getNextFormId(), content);
            k3CloudSaveExtendDto.setSuccess(true);
        } else {
            k3CloudSaveExtendDto.setSuccess(false);
        }
        return k3CloudSaveExtendDto;
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
