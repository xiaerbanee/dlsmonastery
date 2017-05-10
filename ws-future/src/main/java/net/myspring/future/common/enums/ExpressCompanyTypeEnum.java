package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by admin on 2017/2/20.
 */
public enum ExpressCompanyTypeEnum {
    所有,手机订单,物料订单;
    /*ALL,MOBIE_ORDER,MATERIAL_ORDER*/

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(ExpressCompanyTypeEnum expressCompanyTypeEnum:ExpressCompanyTypeEnum.values()){
                list.add(expressCompanyTypeEnum.name());
            }
        }
        return list;
    }
}
