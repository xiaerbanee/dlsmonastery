package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by zhangyf on 2017/6/5.
 */
public enum OfficeRuleEnum {
    办事处,考核区域,业务单元;

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
