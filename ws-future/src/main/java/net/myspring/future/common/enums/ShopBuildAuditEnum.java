package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by zhangyf on 2017/7/7.
 */
public enum ShopBuildAuditEnum {
    全部,待批需要我审核;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(ShopBuildAuditEnum each : ShopBuildAuditEnum.values()){
                list.add(each.name());
            }
        }
        return list;
    }
}
