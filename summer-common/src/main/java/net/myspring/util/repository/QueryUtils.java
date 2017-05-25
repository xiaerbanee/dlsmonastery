package net.myspring.util.repository;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import net.myspring.util.reflect.ReflectionUtil;

import javax.persistence.Query;
import java.lang.reflect.Method;
import java.util.List;

public class QueryUtils {

    public static void setParameter(Query query,Object object) {
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
