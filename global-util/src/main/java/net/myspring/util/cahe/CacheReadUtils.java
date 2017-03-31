package net.myspring.util.cahe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.reflect.ReflectionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/31.
 */
public class CacheReadUtils {
    //表结构缓存
    private static Map<String,List<CacheInputField>> cacheInputMap = Maps.newHashMap();

    public static void initCacheInput(RedisTemplate redisTemplate, Object object)  {
        initCacheInput(redisTemplate,Lists.newArrayList(object));
    }

    public static void initCacheInput(RedisTemplate redisTemplate,Collection<Object> objects) {
        Class  clazz = null;
        List<Object> list = null;
        if(CollectionUtils.isNotEmpty(objects)) {
            list = Lists.newArrayList(objects);
            clazz = list.get(0).getClass();
        }
        String cacheInputKey = clazz.getName();
        if(!cacheInputMap.containsKey(cacheInputKey)) {
            cacheInputMap.put(cacheInputKey,getCacheFieldList(clazz));
        }
        List<CacheInputField> cacheInputFieldList = cacheInputMap.get(cacheInputKey);
        //key所对应的需要设置数据的field
        Map<String,CacheInputObject> cacheInputObjectMap = Maps.newHashMap();
        //所有的key
        List<String> keyList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(cacheInputFieldList)) {
            for(Object object:list) {
                for(CacheInputField cacheInputField:cacheInputFieldList) {
                    String keyPrefix = cacheInputField.getInputKey() + ":";
                    List<String> keySuffixList = Lists.newArrayList();
                    if(cacheInputField.getCollection()) {
                        keySuffixList = ReflectionUtil.getFieldValue(object,cacheInputField.getInputField());
                    } else {
                        keySuffixList.add(ReflectionUtil.getFieldValue(object,cacheInputField.getInputField()));
                    }
                    for(String keySuffix:keySuffixList) {
                        if(StringUtils.isNotBlank(keySuffix)) {
                            String key = keyPrefix + keySuffix;
                            keyList.add(key);
                            CacheInputObject cacheInputObject = new CacheInputObject();
                            cacheInputObject.setObject(object);
                            cacheInputObject.setCacheInputField(cacheInputField);
                            cacheInputObjectMap.put(key,cacheInputObject);
                        }
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(keyList)) {
            List<byte[]> cacheList = getCacheList(redisTemplate,keyList);
            for(int i =0;i<keyList.size();i++) {
                String key = keyList.get(i);
                CacheInputObject cacheInputObject = cacheInputObjectMap.get(key);
                byte[] cache = cacheList.get(i);
                Object object = deSerialize(cache);
                if(object != null) {
                    Object cacheInputFieldValue = ReflectionUtil.getFieldValue(object,cacheInputObject.getCacheInputField().getOutputInstance());
                    Object localFieldValue = ReflectionUtil.getFieldValue(cacheInputObject.getObject(),cacheInputObject.getCacheInputField().getOutputField());
                    if(cacheInputObject.getCacheInputField().getCollection()) {
                        ((Collection)localFieldValue).add(cacheInputFieldValue);
                    } else {
                        ReflectionUtils.setField(cacheInputObject.getCacheInputField().getOutputField(),cacheInputObject.getObject(),cacheInputFieldValue);
                    }
                }
            }
        }
    }

    public static List<byte[]> getCacheList(RedisTemplate redisTemplate,List<String> keyList){
        RedisCallback<List<Object>> pipelineCallback = connection -> {
            connection.openPipeline();
            for(String key:keyList) {
                connection.get(key.getBytes());
            }
            return connection.closePipeline();
        };
        List<byte[]> results = (List<byte[]>)redisTemplate.execute(pipelineCallback);
        return  results;
    }


    public static Object deSerialize(byte[] bytes){
        Object object=null;
        if(bytes != null){
            try {
                ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
                ObjectInputStream ois=new ObjectInputStream(byteArrayInputStream);
                object=ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    private static List<CacheInputField> getCacheFieldList(Class clazz) {
        List<CacheInputField> cacheInputFieldList = Lists.newArrayList();
        List<Field> fields = Lists.newArrayList();
        ReflectionUtil.getFields(fields,clazz);
        for(Field field:fields) {
            CacheInput  cacheInput = field.getAnnotation(CacheInput.class);
            if(cacheInput != null) {
                CacheInputField cacheInputField = new CacheInputField();
                field.setAccessible(true);
                cacheInputField.setOutputField(field);
                cacheInputField.setInputInstance(cacheInput.inputInstance());
                cacheInputField.setInputField(ReflectionUtils.findField(clazz,cacheInputField.getInputKey()));
                cacheInputField.getInputField().setAccessible(true);
                cacheInputField.setInputKey(cacheInput.inputKey());
                cacheInputField.setOutputInstance(cacheInput.outputInstance());
                boolean collection = false;
                if(Collection.class.isAssignableFrom(field.getType()) ) {
                    collection = true;
                }
                cacheInputField.setCollection(collection);
                cacheInputFieldList.add(cacheInputField);
            }
        }
        return cacheInputFieldList;
    }

}
