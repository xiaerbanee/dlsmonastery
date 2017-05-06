package net.myspring.common.cache;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * Created by liuj on 2017/5/4.
 */
public class MyKeyRedisSerializer implements RedisSerializer<Object> {

    private final Charset charset;

    public MyKeyRedisSerializer() {
        this(Charset.forName("UTF8"));
    }

    public MyKeyRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(Object object) throws SerializationException {
        String string = null;
        if(object != null) {
            string = object.toString();
        }
        return (string == null ? null : string.getBytes(charset));
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return (bytes == null ? null : new String(bytes, charset));
    }
}
