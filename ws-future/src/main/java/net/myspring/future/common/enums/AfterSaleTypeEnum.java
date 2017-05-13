package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by wangzm on 2017/5/12.
 */
public enum  AfterSaleTypeEnum {
    售后机,窜货机;

    private static List<String> list = Lists.newArrayList();

    public static List<String> getList() {
        if (CollectionUtil.isEmpty(list)) {
            for (AfterSaleTypeEnum each : AfterSaleTypeEnum.values()) {
                list.add(each.name());
            }
        }
        return list;
    }
}
