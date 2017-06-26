package net.myspring.uaa.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by wangzm on 2017/4/24.
 */
public enum OfficeTypeEnum {
    业务部门,
    职能部门;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(OfficeTypeEnum officeRuleEnum:OfficeTypeEnum.values()){
                list.add(officeRuleEnum.name());
            }
        }
        return list;
    }
}
