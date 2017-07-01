package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum  CarrierOrderStatusEnum {
    空值, 已导入, 待付款, 验证中, 验证失败, 问题单号, 坏单;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(CarrierOrderStatusEnum carrierOrderStatusEnum:CarrierOrderStatusEnum.values()){
                list.add(carrierOrderStatusEnum.name());
            }
        }
        return list;
    }
}
