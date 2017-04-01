package net.myspring.util.cahe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.collection.CollectionUtil;
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
import java.util.Set;

/**
 * Created by liuj on 2017/3/31.
 */
public class CacheReadUtils {
    //表结构缓存
    private static Map<String, List<CacheInputField>> cacheInputMap = Maps.newHashMap();

    public static void initCacheInput(RedisTemplate redisTemplate, Object object) {
        initCacheInput(redisTemplate, Lists.newArrayList(object));
    }

    public static void initCacheInput(RedisTemplate redisTemplate, Collection objects) {
        Class clazz = null;
        List<Object> list = null;
        if (CollectionUtils.isNotEmpty(objects)) {
            list = Lists.newArrayList(objects);
            clazz = list.get(0).getClass();
        }
        String cacheInputKey = clazz.getName();
        if (!cacheInputMap.containsKey(cacheInputKey)) {
            cacheInputMap.put(cacheInputKey, getCacheFieldList(clazz));
        }
        List<CacheInputField> cacheInputFieldList = cacheInputMap.get(cacheInputKey);
        //key所对应的需要设置数据的field
        List<CacheInputObject> cacheInputObjectList = Lists.newArrayList();
        //所有的key
        Set<String> keySet = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(cacheInputFieldList)) {
            for (Object object : list) {
                for (CacheInputField cacheInputField : cacheInputFieldList) {
                    String keyPrefix = cacheInputField.getInputKey() + ":";
                    List<String> keySuffixList = Lists.newArrayList();
                    if (cacheInputField.getCollection()) {
                        keySuffixList = ReflectionUtil.getFieldValue(object, cacheInputField.getInputField());
                    } else {
                        keySuffixList.add(ReflectionUtil.getFieldValue(object, cacheInputField.getInputField()));
                    }
                    if(CollectionUtil.isNotEmpty(keySuffixList)){
                        CacheInputObject cacheInputObject = new CacheInputObject();
                        cacheInputObject.setObject(object);
                        cacheInputObject.setCacheInputField(cacheInputField);
                        for (String keySuffix : keySuffixList) {
                            if (StringUtils.isNotBlank(keySuffix)) {
                                String key = keyPrefix + keySuffix;
                                keySet.add(key);
                                cacheInputObject.getKeyList().add(key);
                            }
                        }
                        cacheInputObjectList.add(cacheInputObject);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(keySet)) {
            List<String> keyList = Lists.newArrayList(keySet);
            List<byte[]> cacheList = getCacheList(redisTemplate, keyList);
            Map<String,Object> keyMap = Maps.newHashMap();
            for (int i = 0; i < keyList.size(); i++) {
                byte[] cache = cacheList.get(i);
                Object object = deSerialize(cache);
                if (object != null) {
                    keyMap.put(keyList.get(i),object);
                }
            }
            for(CacheInputObject cacheInputObject:cacheInputObjectList) {
                if(CollectionUtils.isNotEmpty(cacheInputObject.getKeyList())) {
                    boolean isCollection = cacheInputObject.getCacheInputField().getCollection();
                    Object localFieldValue = ReflectionUtil.getFieldValue(cacheInputObject.getObject(), cacheInputObject.getCacheInputField().getOutputField());
                    for(String key:cacheInputObject.getKeyList()) {
                        if(keyMap.containsKey(key)) {
                            Object cacheInputFieldValue = ReflectionUtil.getFieldValue(keyMap.get(key),cacheInputObject.getCacheInputField().getOutputInstance());
                            if(isCollection) {
                                if (CollectionUtil.isEmpty((Collection) localFieldValue)) {
                                    localFieldValue = Lists.newArrayList();
                                }
                                ((Collection) localFieldValue).add(cacheInputFieldValue);
                                ReflectionUtils.setField(cacheInputObject.getCacheInputField().getOutputField(), cacheInputObject.getObject(), localFieldValue);
                            } else {
                                ReflectionUtils.setField(cacheInputObject.getCacheInputField().getOutputField(), cacheInputObject.getObject(), cacheInputFieldValue);
                            }
                        }
                    }
                }
            }
        }
    }

    public static List<byte[]> getCacheList(RedisTemplate redisTemplate, List<String> keyList) {
        RedisCallback<List<Object>> pipelineCallback = connection -> {
            connection.openPipeline();
            for (String key : keyList) {
                connection.get(key.getBytes());
            }
            return connection.closePipeline();
        };
        List<byte[]> results = (List<byte[]>) redisTemplate.execute(pipelineCallback);
        return results;
    }


    public static Object deSerialize(byte[] bytes) {
        Object object = null;
        if (bytes != null) {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
                object = ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    private static List<CacheInputField> getCacheFieldList(Class clazz) {
        List<CacheInputField> cacheInputFieldList = Lists.newArrayList();
        List<Field> fields = Lists.newArrayList();
        ReflectionUtil.getFields(fields, clazz);
        for (Field field : fields) {
            CacheInput cacheInput = field.getAnnotation(CacheInput.class);
            if (cacheInput != null) {
                CacheInputField cacheInputField = new CacheInputField();
                field.setAccessible(true);
                cacheInputField.setOutputField(field);
                cacheInputField.setInputInstance(cacheInput.inputInstance());
                cacheInputField.setInputField(ReflectionUtils.findField(clazz, cacheInput.inputInstance()));
                cacheInputField.getInputField().setAccessible(true);
                cacheInputField.setInputKey(cacheInput.inputKey());
                cacheInputField.setOutputInstance(cacheInput.outputInstance());
                boolean collection = false;
                if (Collection.class.isAssignableFrom(field.getType())) {
                    collection = true;
                }
                cacheInputField.setCollection(collection);
                cacheInputFieldList.add(cacheInputField);
            }
        }
        return cacheInputFieldList;
    }

}
