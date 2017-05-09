package net.myspring.cloud.modules.input.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.KingdeeActionEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynExtendDto;
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
public class KingdeeManager {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, OkHttpClient> okHttpClientMap = Maps.newHashMap();


    public Boolean login(String postUrl,String dbid,String username,String password) {
        List<Object> list = Lists.newArrayList();
        list.add(dbid);
        list.add(username);
        list.add(password);
        list.add("2052");
        OkHttpClient okHttpClient = getClient(RequestUtils.getAccountId());
        Request request = new Request.Builder()
                .url(postUrl + KingdeeActionEnum.VALIDATE_USER.getValue())
                .addHeader("content-type", "application/json;charset:utf-8")
                .put(getRequestBody(list))
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            logger.debug("login result:"+ result );
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
        OkHttpClient okHttpClient = getClient(RequestUtils.getAccountId());
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            response.body().close();
            return result;
        } catch (IOException e) {
            return "";
        }
    }

    public KingdeeSynDto save(KingdeeSynDto kingdeeSynDto) {
        KingdeeBook kingdeeBook = kingdeeSynDto.getKingdeeBook();
        Map<String,String> resultMap = Maps.newHashMap();
        String result = invoke(kingdeeBook.getKingdeePostUrl(), KingdeeActionEnum.SAVE.getValue(),kingdeeSynDto.getFormId(),kingdeeSynDto.getContent());
        resultMap.put("SAVE",result);
        JSONObject jsonObject = JSONObject.fromObject(result);
        if (BoolEnum.TRUE.getName().equals(jsonObject.getJSONObject("Result").getJSONObject("ResponseStatus").getString("IsSuccess"))) {
            String billNo = jsonObject.getJSONObject("Result").getString("Number");
            Map<String, Object> root = Maps.newLinkedHashMap();
            root.put("CreateOrgId", 0);
            root.put("Numbers", billNo);
            String content = ObjectMapperUtils.writeValueAsString(root);
            result = invoke(kingdeeBook.getKingdeePostUrl(), KingdeeActionEnum.SUBMIT.getValue(),kingdeeSynDto.getFormId(), content);
            resultMap.put("SUBMIT",result);
            if (kingdeeSynDto.getAutoAudit()) {
                result = invoke(kingdeeBook.getKingdeePostUrl(), KingdeeActionEnum.AUDIT.getValue(),kingdeeSynDto.getFormId(), content);
                resultMap.put("AUDIT",result);
            }
            kingdeeSynDto.setBillNo(billNo);
            kingdeeSynDto.setSuccess(true);
        } else {
            kingdeeSynDto.setSuccess(false);
        }
        kingdeeSynDto.setResult(ObjectMapperUtils.writeValueAsString(resultMap));
        return kingdeeSynDto;
    }


    public KingdeeSynExtendDto save(KingdeeSynExtendDto kingdeeSynExtendDto) {
        KingdeeBook kingdeeBook = kingdeeSynExtendDto.getKingdeeBook();
        Map<String,String> resultMap = Maps.newHashMap();
        String result = invoke(kingdeeBook.getKingdeePostUrl(), KingdeeActionEnum.SAVE.getValue(),kingdeeSynExtendDto.getFormId(),kingdeeSynExtendDto.getContent());
        resultMap.put("SAVE",result);
        JSONObject jsonObject = JSONObject.fromObject(result);
        if (BoolEnum.TRUE.getName().equals(jsonObject.getJSONObject("Result").getJSONObject("ResponseStatus").getString("IsSuccess"))) {
            String billNo = jsonObject.getJSONObject("Result").getString("Number");
            Map<String, Object> root = Maps.newLinkedHashMap();
            root.put("CreateOrgId", 0);
            root.put("Numbers", billNo);
            String content = ObjectMapperUtils.writeValueAsString(root);
            result = invoke(kingdeeBook.getKingdeePostUrl(), KingdeeActionEnum.SUBMIT.getValue(),kingdeeSynExtendDto.getFormId(), content);
            resultMap.put("SUBMIT",result);
            result =  invoke(kingdeeBook.getKingdeePostUrl(), KingdeeActionEnum.AUDIT.getValue(),kingdeeSynExtendDto.getFormId(), content);
            resultMap.put("AUDIT",result);
            kingdeeSynExtendDto.setBillNo(billNo);
            String nextBillNo = kingdeeSynExtendDto.getNextBillNo();
            root = Maps.newLinkedHashMap();
            root.put("CreateOrgId", 0);
            root.put("Numbers", nextBillNo);
            kingdeeSynExtendDto.setBillNo(kingdeeSynExtendDto.getBillNo() + CharConstant.COMMA + nextBillNo);
            content = ObjectMapperUtils.writeValueAsString(root);
            result = invoke(kingdeeBook.getKingdeePostUrl(), KingdeeActionEnum.SUBMIT.getValue(),kingdeeSynExtendDto.getNextFormId(), content);
            resultMap.put("NEXT_SUBMIT",result);
            result = invoke(kingdeeBook.getKingdeePostUrl(), KingdeeActionEnum.AUDIT.getValue(),kingdeeSynExtendDto.getNextFormId(), content);
            resultMap.put("NEXT_AUDIT",result);
            kingdeeSynExtendDto.setSuccess(true);
        } else {
            kingdeeSynExtendDto.setSuccess(false);
        }
        kingdeeSynExtendDto.setResult(ObjectMapperUtils.writeValueAsString(result));
        return kingdeeSynExtendDto;
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
