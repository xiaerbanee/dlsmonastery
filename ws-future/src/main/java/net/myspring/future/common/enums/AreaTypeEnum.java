package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum AreaTypeEnum {
    乡镇,
    县城,
    市区;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(AreaTypeEnum areaTypeEnum:AreaTypeEnum.values()){
                list.add(areaTypeEnum.name());
            }
        }
        return list;
    }
}
