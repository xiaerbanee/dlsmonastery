package net.myspring.util.repository;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.Query;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryUtils {

    private static MySQLDialect mySQLDialect = new MySQLDialect();

    private static SQLServerDialect sqlServerDialect = new SQLServerDialect();

    public static void setParameter(Query query,Object... objects) {
        for(Object object:objects) {
            List<Method> methodList= Lists.newArrayList();
            ReflectionUtil.getMethods(methodList,object.getClass());
            for(Method method:methodList) {
                if(method.getName().startsWith("get") && method.getParameterCount()==0) {
                    try {
                        String fieldName = method.getName().replaceFirst("get","");
                        fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,fieldName);
                        query.setParameter(fieldName,method.invoke(object));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public static Map<String,Object> getParamMap(Object... objects) {
        Map<String,Object> map = Maps.newHashMap();
        for(Object object:objects) {
            List<Method> methodList= Lists.newArrayList();
            ReflectionUtil.getMethods(methodList,object.getClass());
            for(Method method:methodList) {
                if(method.getName().startsWith("get") && method.getParameterCount()==0) {
                    try {
                        String fieldName = method.getName().replaceFirst("get","");
                        fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,fieldName);
                        map.put(fieldName,method.invoke(object));
                    } catch (Exception e) {
                    }
                }
            }
        }
        return map;
    }


    public static MySQLDialect getMySQLDialect() {
        return mySQLDialect;
    }

    public static SQLServerDialect getSqlServerDialect() {
        return sqlServerDialect;
    }
}
