package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by Lenovo on 2017/4/6.
 */
public enum AccountChangeTypeEnum {
    // 基础信息
    MOBILE_PHONE,
    ID_CARD,
    BANK_CARD,
    BASE_SALARY,
    // 调职调岗
    OFFICE,
    POSITION,
    LEADER,
    REGULAR_WORKER,
    ENTRY_WORKER,
    LEAVE_WORKER;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(AccountChangeTypeEnum accountChangeTypeEnum:AccountChangeTypeEnum.values()){
                list.add(accountChangeTypeEnum.name());
            }
        }
        return list;
    }
}
