package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by wangzm on 2017/6/7.
 */
public enum SumTypeEnum {
    区域,
    型号;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(SumTypeEnum sumTypeEnum:SumTypeEnum.values()){
                list.add(sumTypeEnum.name());
            }
        }
        return list;
    }
}
