package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by Lenovo on 2017/4/6.
 */
public enum AccountChangeTypeEnum {
    // 基础信息
    手机,
    身份证,
    银行卡号,
    底薪,
    // 调职调岗
    部门,
    岗位,
    上级,
    转正,
    入职,
    离职;

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
