package net.myspring.util.repository;

import net.myspring.util.reflect.ReflectionUtil;

import javax.persistence.Query;
import java.lang.reflect.Field;

public class QueryUtils {

    public void setParameter(Query query,Object object) {
         for(Field field:object.getClass().getDeclaredFields()) {
             query.setParameter(field.getName(),ReflectionUtil.getFieldValue(object,field));
         }
    }

}
