package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum ExpressOrderTypeEnum {
        手机订单, 物料订单,大库调拨;
        /*MOBIE_ORDER,MATERIAL_ORDER,DEPOT_ALLOT*/

        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
                if(CollectionUtil.isEmpty(list)){
                        for(ExpressOrderTypeEnum each : ExpressOrderTypeEnum.values()){
                                list.add(each.name());
                        }
                }
                return list;
        }

}
