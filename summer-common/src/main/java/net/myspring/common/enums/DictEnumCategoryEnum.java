package net.myspring.common.enums;

import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBiMap;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2016/12/31.
 */
public enum DictEnumCategoryEnum {
    DUTY_LEAVE_TYPE("请假类型"),
    SEX("性别"),
    EDUCATION("学历"),
    SHOP_TYPE("店面类型"),
    OPEN_TYPE("卖场目前情况"),
    BUILD_TYPE("项目建设方式"),
    FIXTURE_TYPE("装修类别"),
    PACKAGES_STATUS("包装"),
    BAD_TYPE("坏机类型"),
    TOS_TORE_TYPE("退机类型"),
    MEMORY("内存"),
    SHOP_ATTRIBUTE_TYPE("终端类型"),
    SHOP_MONTH_TOTAL("店月总量");

    private static HashBiMap<String,String> map = HashBiMap.create();
    private String value;

    DictEnumCategoryEnum(String value) {
        this.value = value;
    }

    public static HashBiMap<String,String> getMap(){
        for(DictEnumCategoryEnum dictEnumCategoryEnum:DictEnumCategoryEnum.values()){
            map.put(dictEnumCategoryEnum.name(),dictEnumCategoryEnum.getValue());
        }
        return map;
    }

    public String getValue() {
        return value;
    }
}
