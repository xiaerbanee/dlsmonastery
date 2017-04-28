package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/24.
 */
public enum OfficeRuleEnum {
    BUSINESS_OFFICE,
    SUPPORT_OFFICE;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(OfficeRuleEnum officeRuleEnum:OfficeRuleEnum.values()){
                list.add(officeRuleEnum.name());
            }
        }
        return list;
    }
}
