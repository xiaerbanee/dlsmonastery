package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum ProductImeStockReportOutTypeEnum {
        电子保卡,核销;

        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
                if(CollectionUtil.isEmpty(list)){
                        for(ProductImeStockReportOutTypeEnum each : ProductImeStockReportOutTypeEnum.values()){
                                list.add(each.name());
                        }
                }
                return list;
        }

}
