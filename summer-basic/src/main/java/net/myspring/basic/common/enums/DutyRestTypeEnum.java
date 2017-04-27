package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by liuj on 2016/12/31.
 */
public enum DutyRestTypeEnum {
    OVER_TIME_OFF, ANNUAL_TIME_OFF;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(DutyRestTypeEnum dutyRestTypeEnum:DutyRestTypeEnum.values()){
                list.add(dutyRestTypeEnum.name());
            }
        }
        return list;
    }
}
