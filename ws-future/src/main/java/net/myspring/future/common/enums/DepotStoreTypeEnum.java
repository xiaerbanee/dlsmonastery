package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by Lenovo on 2017/5/17.
 */
public enum DepotStoreTypeEnum {
    好机库,坏机库,寄存机库,淘汰机库;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(DepotStoreTypeEnum depotStoreTypeEnum:DepotStoreTypeEnum.values()){
                list.add(depotStoreTypeEnum.name());
            }
        }
        return list;
    }

}
