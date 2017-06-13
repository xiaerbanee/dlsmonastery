package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum  AfterSaleDetailTypeEnum {

    地区录入,总部录入,工厂录入;

    private static List<String> list = Lists.newArrayList();

    public static List<String> getList() {
        if (CollectionUtil.isEmpty(list)) {
            for (AfterSaleDetailTypeEnum each : AfterSaleDetailTypeEnum.values()) {
                list.add(each.name());
            }
        }
        return list;
    }
}
