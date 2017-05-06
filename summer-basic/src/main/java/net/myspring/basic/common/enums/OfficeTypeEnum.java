package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by wangzm on 2017/4/24.
 */
public enum OfficeTypeEnum {
    BUSINESS,
    SUPPORT;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(OfficeTypeEnum officeTypeEnum: OfficeTypeEnum.values()){
                list.add(officeTypeEnum.name());
            }
        }
        return list;
    }
}
