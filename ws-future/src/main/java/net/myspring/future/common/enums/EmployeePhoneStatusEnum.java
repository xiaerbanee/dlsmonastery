package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by admin on 2017/2/16.
 */
public enum EmployeePhoneStatusEnum {
    已交,已退;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(EmployeePhoneStatusEnum statusEnum: EmployeePhoneStatusEnum.values()){
                list.add(statusEnum.toString());
            }
        }
        return list;
    }
}
