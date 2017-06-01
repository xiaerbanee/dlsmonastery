package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum ProductImeStockReportSumTypeEnum {
        区域, 型号;

        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
                if(CollectionUtil.isEmpty(list)){
                        for(ProductImeStockReportSumTypeEnum each : ProductImeStockReportSumTypeEnum.values()){
                                list.add(each.name());
                        }
                }
                return list;
        }

}
