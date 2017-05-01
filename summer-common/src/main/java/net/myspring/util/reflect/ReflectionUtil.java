/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package net.myspring.util.reflect;

import java.lang.reflect.Field;
import java.util.List;

public class ReflectionUtil  extends org.springside.modules.utils.reflect.ReflectionUtil{

	//递归获取所有Field
	public static void getFields(List<Field> fields, Class clazz) {
		for(Field field:clazz.getDeclaredFields()) {
			fields.add(field);
		}
		clazz = clazz.getSuperclass();
		if (!clazz.getName().equals(Object.class.getName())) {
			getFields(fields,clazz);
		}
	}

}
