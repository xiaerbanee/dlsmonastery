package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum  OutTypeEnum {
    电子报卡,
    核销;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(OutTypeEnum outTypeEnum:OutTypeEnum.values()){
                list.add(outTypeEnum.name());
            }
        }
        return list;
    }
}
