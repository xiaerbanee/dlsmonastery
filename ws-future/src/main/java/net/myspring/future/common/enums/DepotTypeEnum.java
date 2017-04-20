package net.myspring.future.common.enums;

import com.google.common.collect.HashBiMap;

/**
 * Created by admin on 2016/12/31.
 */
public enum DepotTypeEnum {
    大库_总部(100),
    大库_办事处(110),
    大库_寄售(200),
    大库_代理(500),
    门店_直营(600),
    门店_寄售(610),
    门店_专卖店(620),
    门店_直营_分店(620),
    门店_代理(630);
    private static HashBiMap<Integer, String> map;

    private Integer value;

    DepotTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static HashBiMap<Integer, String> getMap() {
        if (map == null) {
            map = HashBiMap.create();
            for (DepotTypeEnum depotType : DepotTypeEnum.values()) {
                map.put(depotType.getValue(), depotType.name());
            }
        }
        return map;
    }
}
