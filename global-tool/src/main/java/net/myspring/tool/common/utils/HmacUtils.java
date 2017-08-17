package net.myspring.tool.common.utils;

import net.myspring.util.text.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HmacUtils {
    /**
     * 对报文进行hmac-md5签名
     * @param aValue - 字符串
     * @param aKey - 密钥
     * @return - 签名结果，hex字符串
     */
    public static String hmacSign(String aValue, String aKey) {
        return signMD5(aValue, aKey, "UTF-8");
    }

    /**
     * 对报文进行hmac-md5签名
     * @param aValue - 字符串
     * @param aKey - 密钥
     * @param encoding - 字符串编码方式
     * @return - 签名结果，hex字符串
     */
    public static String signMD5(String aValue, String aKey, String encoding) {
        if (StringUtils.isBlank(encoding)){
            encoding = "UTF-8";
        }

        byte k_ipad[] = new byte[64];
        byte k_opad[] = new byte[64];
        byte keyb[];
        byte value[];
        try
        {
            keyb = aKey.getBytes(encoding);
            value = aValue.getBytes(encoding);
        }
        catch(UnsupportedEncodingException e)
        {
            keyb = aKey.getBytes();
            value = aValue.getBytes();
        }

        Arrays.fill(k_ipad, keyb.length, 64, (byte)54);
        Arrays.fill(k_opad, keyb.length, 64, (byte)92);
        for(int i = 0; i < keyb.length; i++)
        {
            k_ipad[i] = (byte)(keyb[i] ^ 0x36);
            k_opad[i] = (byte)(keyb[i] ^ 0x5c);
        }

        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance("MD5");
        }
        catch(NoSuchAlgorithmException e)
        {
            return null;
        }
        md.update(k_ipad);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return StringUtils.toHex(dg);
    }

}
