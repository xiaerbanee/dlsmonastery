package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by wangzm on 2017/7/11.
 */
public enum GoodsOrderPullStatusEnum {
    空值,已推送,待发货,待开单;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(GoodsOrderPullStatusEnum each : GoodsOrderPullStatusEnum.values()){
                list.add(each.name());
            }
        }
        return list;
    }
}
