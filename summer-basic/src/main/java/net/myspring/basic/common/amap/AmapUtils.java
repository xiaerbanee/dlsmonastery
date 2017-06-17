package net.myspring.basic.common.amap;

import net.myspring.basic.common.amap.bo.RegeoBo;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by liuj on 2016/12/7.
 */
public class AmapUtils {

    private static OkHttpClient okHttpClient=new OkHttpClient.Builder().build();

    public static RegeoBo getRegeoBo(String longitude, String latitude, String key, Integer radius, String extensions) {
        String url = "http://restapi.amap.com/v3/geocode/regeo?output=json&location=" + longitude+ "," +latitude+ "&key=" + key + "&radius=" + radius + "&extensions=" + extensions;
        Request request=new Request.Builder().url(url).build();
        Response response;
        String result = "";
        RegeoBo regeoBo = null;
        try {
            response = okHttpClient.newCall(request).execute();
            result=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(StringUtils.isNotBlank(result)) {
            result = StringUtils.replace(result,"[]","\"\"");
            result = StringUtils.replace(result,"\"pois\":\"\"","\"pois\":[]");
            regeoBo = ObjectMapperUtils.readValue(result,RegeoBo.class);
        }
        return regeoBo;
    }
}
