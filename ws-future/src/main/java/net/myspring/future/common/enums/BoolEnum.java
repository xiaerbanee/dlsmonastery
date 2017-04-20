package net.myspring.future.common.enums;

import com.google.common.collect.Maps;

import java.util.Map;

public enum BoolEnum {
	TRUE(true, "1"),
	FALSE(false, "0");

	private static Map<Boolean, String> map;

	BoolEnum(Boolean name, String value) {
		this.name = name;
		this.value = value;
	}

	private Boolean name;
	private String value;

	public Boolean getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public static Map<Boolean,String> getMap() {
		if (map == null) {
			map = Maps.newLinkedHashMap();
			map.put(BoolEnum.TRUE.getName(), BoolEnum.TRUE.getValue());
			map.put(BoolEnum.FALSE.getName(), BoolEnum.FALSE.getValue());
		}
		return map;
	}
}
