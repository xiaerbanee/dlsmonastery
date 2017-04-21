package net.myspring.future.common.enums;

import com.google.common.collect.HashBiMap;

import java.util.Map;

public enum DataScopeEnum {
	ALL("所有数据", 10),
	OFFICE_AND_CHILD("部门及以下数据", 20),
	OFFICE("部门数据", 30),
	SELF("个人数据", 40);

	private static HashBiMap<Integer,String> map ;

	DataScopeEnum(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	private String name;
	private Integer value;

	public String getName() {
		return name;
	}

	public Integer getValue() {
		return value;
	}

	public static Map<Integer, String> getMap() {
		if (map == null) {
			map = HashBiMap.create();
			for (DataScopeEnum dataScope : DataScopeEnum.values()) {
				map.put(dataScope.getValue(), dataScope.getName());
			}
		}
		return map;
	}
}
