package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum StoreAllotStatusEnum {
        待发货, 发货中, 已完成;
        /*SHIPPING,SHIPPED,FINISH*/

        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
                if(CollectionUtil.isEmpty(list)){
                        for(StoreAllotStatusEnum each : StoreAllotStatusEnum.values()){
                                list.add(each.name());
                        }
                }
                return list;
        }

}
