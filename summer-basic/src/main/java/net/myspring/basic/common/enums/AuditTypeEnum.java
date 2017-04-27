package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by liuj on 2016/12/31.
 */
public enum AuditTypeEnum {
    APPLYING,
    PASSED,
    NOT_PASS;

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