package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum ProductImeHistoryTypeEnum {
        串码调拨,售后调拨,货品订货,大库调拨,串码上报,上报退回,串码核销,核销退回;


        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
                if(CollectionUtil.isEmpty(list)){
                        for(ProductImeHistoryTypeEnum each : ProductImeHistoryTypeEnum.values()){
                                list.add(each.name());
                        }
                }
                return list;
        }

}
