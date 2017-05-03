package net.myspring.util.cahe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import net.myspring.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ReflectionUtils;
import net.myspring.util.collection.CollectionUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuj on 2017/3/31.
 */
public class CacheReadUtils {
    private static Logger logger = LoggerFactory.getLogger(CacheReadUtils.class);
    //表结构缓存
    private static Map<String, List<CacheInputField>> cacheInputMap = Maps.newHashMap();

    public static void initCacheInput(RedisTemplate redisTemplate, Object object) {
        initCacheInput(redisTemplate, Lists.newArrayList(object));
    }

    public static void initCacheInput(RedisTemplate redisTemplate, Collection objects) {
        LocalDateTime start = LocalDateTime.now();
        logger.info("read cache start at " + LocalDateTimeUtils.format(start,LocalDateTimeUtils.FORMATTER_MILLISECOND));
        Class clazz;
        List<Object> list;
        if (CollectionUtil.isNotEmpty(objects)) {
            list = Lists.newArrayList(objects);
            clazz = list.get(0).getClass();
            String cacheInputKey = clazz.getName();
            if (!cacheInputMap.containsKey(cacheInputKey)) {
                cacheInputMap.put(cacheInputKey, getCacheFieldList(clazz));
            }
            List<CacheInputField> cacheInputFieldList = cacheInputMap.get(cacheInputKey);
            //key所对应的需要设置数据的field
            List<CacheInputObject> cacheInputObjectList = Lists.newArrayList();
            //所有的key
            Set<String> keySet = Sets.newHashSet();
            if (CollectionUtil.isNotEmpty(cacheInputFieldList)) {
                for (Object object : list) {
                    for (CacheInputField cacheInputField : cacheInputFieldList) {
                        String keyPrefix = cacheInputField.getInputKey() + ":";
                        List<String> keySuffixList = Lists.newArrayList();
                        if (cacheInputField.getCollection()) {
                            keySuffixList = ReflectionUtil.getProperty(object, cacheInputField.getInputField().getName());
                        } else {
                            keySuffixList.add(ReflectionUtil.getProperty(object, cacheInputField.getInputField().getName()));
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
            if (CollectionUtil.isNotEmpty(keySet)) {
                List<String> keyList = Lists.newArrayList(keySet);
                Map<String,Object> cacheMap = getCacheMap(redisTemplate,keyList);
                for(CacheInputObject cacheInputObject:cacheInputObjectList) {
                    if(CollectionUtil.isNotEmpty(cacheInputObject.getKeyList())) {
                        boolean isCollection = cacheInputObject.getCacheInputField().getCollection();
                        for(String key:cacheInputObject.getKeyList()) {
                            if(cacheMap.containsKey(key)) {
                                Map<String,Object> map = (Map<String, Object>) ((List)cacheMap.get(key)).get(1);
                                Object cacheInputFieldValue = map.get(cacheInputObject.getCacheInputField().getOutputInstance());
                                Field outputField =cacheInputObject.getCacheInputField().getOutputField();
                                if(isCollection) {
                                    Object localFieldValue = ReflectionUtil.getFieldValue(cacheInputObject.getObject(), outputField);
                                    if (CollectionUtil.isEmpty((Collection) localFieldValue)) {
                                        localFieldValue = Lists.newArrayList();
                                    }
                                    ((Collection) localFieldValue).add(getConvertObject(cacheInputFieldValue,outputField.getClass()));
                                    ReflectionUtils.setField(outputField, cacheInputObject.getObject(), localFieldValue);
                                } else {
                                    ReflectionUtils.setField(outputField, cacheInputObject.getObject(), getConvertObject(cacheInputFieldValue,outputField.getType()));
                                }
                            }
                        }
                    }
                }
            }
            LocalDateTime end = LocalDateTime.now();
            logger.info("read cache end at " + LocalDateTimeUtils.format(end,LocalDateTimeUtils.FORMATTER_MILLISECOND));
            logger.info("read cache in " + ChronoUnit.MILLIS.between(start, end) + " mills");
        }
    }

    private static Object getConvertObject(Object input,Class clazz) {
        Object result;
        if(input instanceof String) {
           String inputStr = (String)input;
            if(clazz == String.class) {
                result =inputStr;
            } else if(clazz == LocalDate.class) {
                result = LocalDateUtils.parse(inputStr);
            } else if(clazz == LocalDateTime.class) {
                result = LocalDateTimeUtils.parse(inputStr);
            } else if(clazz == LocalTime.class) {
                result = LocalTime.parse(inputStr);
            } else if(clazz == Integer.class) {
                result = Integer.valueOf(inputStr);
            } else if(clazz == Long.class) {
                result = Long.valueOf(inputStr);
            } else if(clazz == Double.class) {
                result = Double.valueOf(inputStr);
            } else if(clazz == BigDecimal.class) {
                result = new BigDecimal(inputStr);
            } else if(clazz == Boolean.class) {
                result = Boolean.valueOf(inputStr);
            } else if(clazz == Float.class) {
                result = Float.valueOf(inputStr);
            } else if(clazz == Short.class) {
                result = Short.valueOf(inputStr);
            } else {
                result =inputStr;
            }
        } else {
            result = input;
        }
        return result;
    }

    private static Map<String,Object> getCacheMap(RedisTemplate redisTemplate, List<String> keyList) {
        RedisCallback<List<Object>> pipelineCallback = connection -> {
            connection.openPipeline();
            for (String key : keyList) {
                connection.get(key.getBytes());
            }
            return connection.closePipeline();
        };
        List<byte[]> cacheList = (List<byte[]>) redisTemplate.execute(pipelineCallback);
        Map<String,Object> cacheMap = Maps.newHashMap();
        for (int i = 0; i < keyList.size(); i++) {
            byte[] cache = cacheList.get(i);
            Object object = deSerialize(cache);
            if (object != null) {
                cacheMap.put(keyList.get(i),object);
            }
        }
        return cacheMap;
    }

    public static Map<String,Object> getCacheMap(RedisTemplate redisTemplate, List<String> keyList,String fieldName) {
        Map<String,Object> cacheMap = getCacheMap(redisTemplate,keyList);
        Map<String,Object> map = Maps.newHashMap();
        for(String key:cacheMap.keySet()) {
            map.put(key,ReflectionUtil.getFieldValue(cacheMap.get(key),fieldName));
        }
        return map;
    }

    private static Object deSerialize(byte[] bytes) {
        Object object = null;
        if (bytes != null) {
            String json = new String(bytes);
            object = ObjectMapperUtils.readValue(json,Object.class);
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
