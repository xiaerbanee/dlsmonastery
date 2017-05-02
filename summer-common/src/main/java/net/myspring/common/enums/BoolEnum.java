package net.myspring.common.enums;

import com.google.common.collect.Maps;

import java.util.Map;

public enum BoolEnum {
	TRUE("true", "1"),
	FALSE("false", "0");

	private static Map<String, String> map;

	BoolEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}

	private String name;
	private String value;

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public static Map<String,String> getMap() {
		if (map == null) {
			map = Maps.newLinkedHashMap();
			map.put(BoolEnum.TRUE.getName(), BoolEnum.TRUE.getValue());
			map.put(BoolEnum.FALSE.getName(), BoolEnum.FALSE.getValue());
		}
		return map;
	}
}
