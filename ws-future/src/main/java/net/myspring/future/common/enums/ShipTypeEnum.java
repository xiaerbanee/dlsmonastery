package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum ShipTypeEnum {
        总部发货, 总部自提, 地区发货,地区自提,代理发货,代理自提;
        /*HEADER_SHIPPING,HEADER_FETCH,AREA_SHIPPING,AREA_FETCH,AGENCY_SHIPPING,AGENCY_FETCH*/

        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
                if(CollectionUtil.isEmpty(list)){
                        for(ShipTypeEnum each : ShipTypeEnum.values()){
                                list.add(each.name());
                        }
                }
                return list;
        }

}
