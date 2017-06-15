package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by admin on 2017/2/17.
 */
public enum EmployeePhoneDepositStatusEnum {

    省公司审核,
    未通过,
    已通过;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(EmployeePhoneDepositStatusEnum statusEnum: EmployeePhoneDepositStatusEnum.values()){
                list.add(statusEnum.toString());
            }
        }
        return list;
    }
}
