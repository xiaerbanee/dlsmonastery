package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by liuj on 2016/12/31.
 */
public enum DictEnumCategoryEnum {
    DUTY_LEAVE_TYPE,
    SEX,
    EDUCATION,
    SHOP_TYPE,
    OPEN_TYPE,
    BUILD_TYPE,
    FIXTURE_TYPE,
    PACKAGES_STATUS,
    BAD_TYPE,
    TOS_TORE_TYPE,
    MEMORY,
    SHOP_ATTRIBUTE_TYPE,
    SHOP_MONTH_TOTAL;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(AccountChangeTypeEnum accountChangeTypeEnum:AccountChangeTypeEnum.values()){
                list.add(accountChangeTypeEnum.name());
            }
        }
        return list;
    }
}
