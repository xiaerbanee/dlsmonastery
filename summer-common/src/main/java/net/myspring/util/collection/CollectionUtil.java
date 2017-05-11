package net.myspring.util.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.util.text.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.*;

/**
 * Created by liuj on 2017/5/1.
 */
public class CollectionUtil extends org.springside.modules.utils.collection.CollectionUtil {



    public static String extractAndJoin(final Collection collection, final String propertyName) {
        return StringUtils.join(extractToList(collection, propertyName),",");
    }

    public static <E> List<E> extractToList(final Collection collection, final String propertyName) {
        if (isEmpty(collection)) {
            return Lists.newArrayList();
        }
        List<E> list = new ArrayList(collection.size());
        try {
            for (Object obj : collection) {
                E value=(E) PropertyUtils.getProperty(obj, propertyName);
                if(obj != null&&value!=null) {
                    list.add(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <K,V> Map<K,V> extractToMap(final Collection collection, final String keyPropertyName) {
        if (isEmpty(collection)) {
            return Maps.<K,V>newHashMap();
        }
        Map<K,V> map = new HashMap<K,V>(collection.size());
        try {
            for (Object obj : collection) {
                K key=(K)PropertyUtils.getProperty(obj, keyPropertyName);
                if(obj != null&&key!=null) {
                    map.put(key, (V)obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static <K,E> Map<K,List<E>> extractToMapList(final Collection collection, final String keyPropertyName) {
        if (isEmpty(collection)) {
            return Maps.<K,List<E>>newHashMap();
        }
        Map<K,List<E>> map = new HashMap<K,List<E>>(collection.size());
        try {
            for (Object obj : collection) {
                K key=(K)PropertyUtils.getProperty(obj, keyPropertyName);
                if(obj != null&&key!=null) {
                    if(!map.containsKey(key)){
                        map.put(key,Lists.newArrayList());
                    }
                    map.get(key).add((E)obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 返回a+b的新List.
     */
    public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
        List<T> result = new ArrayList<T>(a);
        result.addAll(b);
        return result;
    }

    /**
     * 返回a-b的新List.
     */
    public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
        List<T> list = new ArrayList<T>(a);
        for (T element : b) {
            list.remove(element);
        }

        return list;
    }

    /**
     * 返回a与b的交集的新List.
     */
    public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList<T>();
        for (T element : a) {
            if (b.contains(element)) {
                list.add(element);
            }
        }
        return list;
    }

    public static Map<String, Object> getMap(String key, Object value) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(key, value);
        return map;
    }

}
