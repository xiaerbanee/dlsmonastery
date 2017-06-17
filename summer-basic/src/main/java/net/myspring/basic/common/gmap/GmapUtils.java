package net.myspring.basic.common.gmap;

import net.myspring.basic.common.gmap.bo.GeocodeResult;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuj on 2017-03-02.
 */
public class GmapUtils {
    private static OkHttpClient okHttpClient=new OkHttpClient.Builder().connectTimeout(10,TimeUnit.SECONDS).build();

    public static GeocodeResult getGeocodeResult(String longitude, String latitude, String key, String languge) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?language=" + languge+ "&latlng="+ latitude+ "," +longitude+ "&key=" + key;
        Request request=new Request.Builder().url(url).build();
        Response response;
        String result = "";
        GeocodeResult geocodeResult = null;
        try {
            response = okHttpClient.newCall(request).execute();
            result=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(StringUtils.isNotBlank(result)) {
            geocodeResult = ObjectMapperUtils.readValue(result,GeocodeResult.class);
        }
        return geocodeResult;
    }
}
