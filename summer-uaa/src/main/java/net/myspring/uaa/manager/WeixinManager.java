package net.myspring.uaa.manager;

import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuj on 2017/4/11.
 */
@Component
public class WeixinManager {
    @Value("${weixin.appid}")
    private String appID;
    @Value("${weixin.appsecret}")
    private String appSecret;

    private OkHttpClient okHttpClient=new OkHttpClient();
    @Autowired
    private RedisTemplate redisTemplate;

    public WeixinSessionDto findWeixinSessionDto(String weixinCode) {
        String key = "weixinSessions:" + weixinCode;
        redisTemplate.expire(key,24, TimeUnit.HOURS);
        WeixinSessionDto weixinSessionDto = (WeixinSessionDto) redisTemplate.opsForValue().get(key);
        if(weixinSessionDto == null) {
            String httpUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="  + appID + "&secret="  + appSecret + "&js_code=" + weixinCode + "&grant_type=authorization_code";
            Request request=new Request.Builder().url(httpUrl).build();
            Response response;
            String result = "";
            try {
                response = okHttpClient.newCall(request).execute();
                result= response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            weixinSessionDto = ObjectMapperUtils.readValue(result,WeixinSessionDto.class);
            if(StringUtils.isBlank(weixinSessionDto.getErrcode())) {
                redisTemplate.opsForValue().set(key,weixinSessionDto);
            }
        }
        return weixinSessionDto;
    }


}
