/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package net.myspring.util.collection;

import java.util.*;

import com.google.common.collect.*;
import net.myspring.util.collection.type.Pair;

import net.myspring.util.reflect.ReflectionUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * 通用Collection的工具集
 * 
 * 关于List, Map, Queue, Set的特殊工具集，另见特定的Utils.
 * 
 * 另JDK中缺少ComparableComparator和NullComparator，直到JDK8才补上。
 * 
 * 因此平时请使用guava的Ordering,fluentable的API更好用，可以链式设置nullFirst，nullLast,reverse
 * 
 * @see com.google.common.collect.Ordering
 */
public class CollectionUtil {

	/**
	 * 判断是否为空.
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null) || collection.isEmpty();
	}

	/**
	 * 判断是否不为空.
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return (collection != null) && !(collection.isEmpty());
	}

	public static boolean isNotEmpty(Map map) {
		return !isEmpty(map);
	}

	public static boolean isEmpty(Map map) {
		return (map == null) || map.isEmpty();
	}

	/**
	 * 取得Collection的第一个元素，如果collection为空返回null.
	 */
	public static <T> T getFirst(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}
		if (collection instanceof List) {
			return ((List<T>) collection).get(0);
		}
		return collection.iterator().next();
	}

	/**
	 * 获取Collection的最后一个元素，如果collection为空返回null.
	 */
	public static <T> T getLast(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		// 当类型List时，直接取得最后一个元素.
		if (collection instanceof List) {
			List<T> list = (List<T>) collection;
			return list.get(list.size() - 1);
		}

		return Iterators.getLast(collection.iterator());
	}

	/**
	 * 两个集合中的所有元素按顺序相等.
	 */
	public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
		return Iterables.elementsEqual(iterable1, iterable2);
	}

	///////////// 求最大最小值，及Top N, Low N//////////
	/**
	 * 返回无序集合中的最小值，使用元素默认排序
	 */
	public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll) {
		return Collections.min(coll);
	}

	/**
	 * 返回无序集合中的最小值
	 */
	public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp) {
		return Collections.min(coll, comp);
	}

	/**
	 * 返回无序集合中的最大值，使用元素默认排序
	 */
	public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll) {
		return Collections.max(coll);
	}

	/**
	 * 返回无序集合中的最大值
	 */
	public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp) {
		return Collections.max(coll, comp);
	}

	/**
	 * 返回无序集合中的最小值和最大值，使用元素默认排序
	 */
	public static <T extends Object & Comparable<? super T>> Pair<T, T> minAndMax(Collection<? extends T> coll) {
		Iterator<? extends T> i = coll.iterator();
		T minCandidate = i.next();
		T maxCandidate = minCandidate;

		while (i.hasNext()) {
			T next = i.next();
			if (next.compareTo(minCandidate) < 0) {
				minCandidate = next;
			} else if (next.compareTo(maxCandidate) > 0) {
				maxCandidate = next;
			}
		}
		return Pair.of(minCandidate, maxCandidate);
	}

	/**
	 * 返回无序集合中的最小值和最大值
	 */
	public static <T> Pair<T, T> minAndMax(Collection<? extends T> coll, Comparator<? super T> comp) {

		Iterator<? extends T> i = coll.iterator();
		T minCandidate = i.next();
		T maxCandidate = minCandidate;

		while (i.hasNext()) {
			T next = i.next();
			if (comp.compare(next, minCandidate) < 0) {
				minCandidate = next;
			} else if (comp.compare(next, maxCandidate) > 0) {
				maxCandidate = next;
			}
		}

		return Pair.of(minCandidate, maxCandidate);
	}

	/**
	 * 排序最高的N个对象, guava已优化.
	 */
	public static <T extends Comparable> List<T> topN(Iterable<T> coll, int n) {
		return Ordering.natural().greatestOf(coll, n);
	}

	/**
	 * 排序最高的N个对象, guava已优化.
	 */
	public static <T extends Comparable> List<T> topN(Iterable<T> coll, int n, Comparator<? super T> comp) {
		return Ordering.from(comp).greatestOf(coll, n);
	}

	/**
	 * 排序最低的N个对象, guava已优化.
	 */
	public static <T extends Comparable> List<T> bottomN(Iterable<T> coll, int n) {
		return Ordering.natural().leastOf(coll, n);
	}

	/**
	 * 排序最低的N个对象, guava已优化.
	 */
	public static <T extends Comparable> List<T> bottomN(Iterable<T> coll, int n, Comparator<? super T> comp) {
		return Ordering.from(comp).leastOf(coll, n);
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

	public static <P,C> List<P>  joinChildList(List<P> parentList, List<C> childList) {
		String parentKeyField = "id";
		String childClassName = getClassName(childList);
		String parentClassName = getClassName(parentList);
		String parentChildField = WordUtils.uncapitalize(childClassName + "List");
		String childParentKeyFiled =WordUtils.uncapitalize(parentClassName + "Id");
		if(StringUtils.isBlank(childClassName)) {
			return parentList;
		} else {
			return joinChildList(parentList,childList,parentKeyField,parentChildField,childParentKeyFiled);
		}
	}


	public static <P,C> List<P>  joinChildList(List<P> parentList, List<C> childList, String parentKeyField,String parentChildField, String childParentKeyFiled) {
		Map<Object,Collection<C>> childMap = Maps.newHashMap();
		try {
			for(C item:childList) {
				Object key = ReflectionUtil.getFieldValue(item,childParentKeyFiled);
				if(!childMap.containsKey(key)) {
					childMap.put(key,Lists.<C>newArrayList());
				}
				childMap.get(key).add(item);
			}
			for(P item:parentList) {
				Object key =ReflectionUtil.getFieldValue(item,parentKeyField);
				if(childMap.containsKey(key)) {
					Collection<C> property = (Collection<C>)ReflectionUtil.getFieldValue(item,parentChildField);
					property.addAll(childMap.get(key));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parentList;
	}

	public static <P,C> List<P>  joinChild(List<P> parentList, List<C> childList) {
		String parentChildField =  WordUtils.uncapitalize(getClassName(childList));
		String parentChildKeyField = parentChildField + "Id";
		String childKeyField = "id";
		if(StringUtils.isBlank(parentChildField)) {
			return parentList;
		} else {
			return joinChild(parentList,childList,parentChildField,parentChildKeyField,childKeyField);
		}
	}

	public static <P,C> List<P>  joinChild(List<P> parentList, List<C> childList, String parentChildField,String parentChildKeyField, String childKeyField) {
		Map<Object,C> childMap = Maps.newHashMap();
		try {
			for(C item:childList) {
				Object key = ReflectionUtil.getFieldValue(item,childKeyField);
				childMap.put(key,item);
			}
			for(P item:parentList) {
				Object key = ReflectionUtil.getFieldValue(item,parentChildKeyField);
				if(childMap.containsKey(key)) {
					ReflectionUtil.setFieldValue(item,parentChildField,childMap.get(key));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parentList;
	}

	public static  <T>  String getClassName(List<T> list) {
		String className = "";
		if(CollectionUtil.isNotEmpty(list)) {
			for(T t:list) {
				if(t != null) {
					return t.getClass().getSimpleName();
				}
			}
		}
		return className;
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

}
