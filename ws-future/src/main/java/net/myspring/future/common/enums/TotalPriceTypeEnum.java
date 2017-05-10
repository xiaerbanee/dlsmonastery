package net.myspring.future.common.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by admin on 2017/2/14.
 */
public enum TotalPriceTypeEnum {
    按数量, 按面积;
    /*NUMBER,AREA*/

    public static List<String> getValues(){
        List<String> values= Lists.newArrayList();
        for(TotalPriceTypeEnum totalPriceTypeEnum: TotalPriceTypeEnum.values()){
            values.add(totalPriceTypeEnum.toString());
        }
        return values;
    }
}
