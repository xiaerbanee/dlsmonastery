package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum RecruitTypeEnum {

    岗位,来源,初试人,品类,婚育状况,最高学历,复试人;

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
