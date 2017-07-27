package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum  OfficeChnageTypeEnum {

    上级,任务点位,名称;
    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(OfficeChnageTypeEnum officeChnageTypeEnum:OfficeChnageTypeEnum.values()){
                list.add(officeChnageTypeEnum.name());
            }
        }
        return list;
    }
}
