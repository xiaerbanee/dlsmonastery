package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by liuj on 2016/12/31.
 */
public enum DutyDateTypeEnum {
    DAY,
    MORNING,
    AFTERNOON;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(DutyDateTypeEnum dutyDateTypeEnum:DutyDateTypeEnum.values()){
                list.add(dutyDateTypeEnum.name());
            }
        }
        return list;
    }
}
