package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by wangzm on 2017/7/5.
 */
public enum  CarrierShopTypeEnum {

    移动,联信;

    public static List<String> types= Lists.newArrayList();

    public static List<String> getTypes() {
        if(CollectionUtil.isEmpty(types)){
            for(CarrierShopTypeEnum shopType:CarrierShopTypeEnum.values()){
                types.add(shopType.toString());
            }
        }
        return types;
    }

}
