package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by admin on 2017/2/20.
 */
public enum ShopDepositTypeEnum {
    市场保证金,
    形象保证金,
    演示机押金;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(ShopDepositTypeEnum shopDepositTypeEnum:ShopDepositTypeEnum.values()){
                list.add(shopDepositTypeEnum.name());
            }
        }
        return list;
    }


}
