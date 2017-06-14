package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by zhangyf on 2017/6/13.
 */
public enum  DepotChangeEnum {
    名称,价格体系, 有无导购, 是否让利,信用额度;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(DepotChangeEnum depotChangeEnum:DepotChangeEnum.values()){
                list.add(depotChangeEnum.name());
            }
        }
        return list;
    }
}
