package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by Lenovo on 2017/5/17.
 */
public enum DepotStoreTypeEnum {

    good,bad,deposit,disuse;

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
