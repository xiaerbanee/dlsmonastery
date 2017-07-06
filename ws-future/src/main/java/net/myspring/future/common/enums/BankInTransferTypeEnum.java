package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum BankInTransferTypeEnum {
    公对公,个人对公;
    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(BankInTransferTypeEnum activityTypeEnum: BankInTransferTypeEnum.values()){
                list.add(activityTypeEnum.name());
            }
        }
        return list;
    }

}
