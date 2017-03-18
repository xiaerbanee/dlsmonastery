package net.myspring.mybatis.context;

import com.google.common.collect.Maps;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.binding.MapperProxy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by liuj on 2016/11/14.
 */

@Aspect
@Component
@Order(1)
public class ProviderMapperAspect {

    private static Map<String,MapperDefinition> mapperDefinitionMap = Maps.newHashMap();

    private final static Logger logger = LoggerFactory.getLogger(ProviderMapperAspect.class);

    @Pointcut("@annotation(org.apache.ibatis.annotations.Mapper)")
    public void mapperAspect() {
    }

    @Before("mapperAspect()")
    public void mapperAspect(JoinPoint point) {
        Class entityClass = null;
        Object target = point.getTarget();
        if(BaseMapper.class.isAssignableFrom(target.getClass())) {
            MapperProxy mapperProxy = (MapperProxy) Proxy.getInvocationHandler(target);
            Class mapperInterface = getMapperInterface(mapperProxy);
            ParameterizedType parameterizedType = (ParameterizedType) mapperInterface.getGenericInterfaces()[0];
            Type[] types = parameterizedType.getActualTypeArguments();
            try {
                entityClass = Class.forName(types[0].getTypeName());
                if(!mapperDefinitionMap.containsKey(entityClass.getName())) {
                    MapperDefinition mapperDefinition= new MapperDefinition();
                    mapperDefinition.setDomainClass(entityClass);
                    mapperDefinition.setIdClass(Class.forName(types[1].getTypeName()));
                    mapperDefinitionMap.put(entityClass.getName(),mapperDefinition);
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            }
        }
        MapperThreadLocal.get().setMapperDefinition(mapperDefinitionMap.get(entityClass==null?null:entityClass.getName()));
    }

    private Class getMapperInterface(MapperProxy mapperProxy) {
        return null;
    }

}
