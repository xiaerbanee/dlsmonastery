package net.myspring.cloud.common.utils;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Created by liuj on 2016-09-21.
 */
public class Base64Utils {
    /**
     * base64算法加密
     * @param data
     * @return
     */
    public static String encode(byte[] data){
        String result = new BASE64Encoder().encode(data);
        return result;
    }

    /**
     * base64算法解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String decode(String data){
        byte[] resultBytes = new byte[0];
        try {
            resultBytes = new BASE64Decoder().decodeBuffer(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(resultBytes);
    }
}
