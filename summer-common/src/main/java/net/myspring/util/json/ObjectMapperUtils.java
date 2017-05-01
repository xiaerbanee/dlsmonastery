package net.myspring.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuj on 2016-11-01.
 */
public class ObjectMapperUtils {
    private static Logger logger = LoggerFactory.getLogger(ObjectMapperUtils.class);

    private static final ObjectMapper objectMapper = getObjectMapper();

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer());
        javaTimeModule.addDeserializer(LocalDate.class,new LocalDateDeserializer());
        javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer());
        javaTimeModule.addDeserializer(LocalTime.class,new LocalTimeDeserializer());
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return objectMapper;
    }

    public static String writeValueAsString(Object object) {
        return writeValueAsString(objectMapper,object);
    }

    public static String writeValueAsString(ObjectMapper objectMapper,Object object) {
        String result = null;
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public static  <T> T readValue(String json, Class<T> clazz) {
        return readValue(objectMapper, json, clazz);
    }

    public static  <T> T readValue(ObjectMapper objectMapper,String json, Class<T> clazz) {
        T t = null;
        try {
            t = objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return t;
    }
}
