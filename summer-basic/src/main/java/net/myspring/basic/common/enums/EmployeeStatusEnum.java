package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by admin on 2017/2/27.
 */
public enum EmployeeStatusEnum {
    JOB,NOT_IN_JOB;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(EmployeeStatusEnum employeeStatusEnum:EmployeeStatusEnum.values()){
                list.add(employeeStatusEnum.name());
            }
        }
        return list;
    }
}
