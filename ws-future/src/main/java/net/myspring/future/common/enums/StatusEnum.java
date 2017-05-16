package net.myspring.future.common.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by haos on 2017/5/13.
 */
public enum StatusEnum {
    已通过, 未通过;
    /*NUMBER,AREA*/

    public static List<String> getValues(){
        List<String> values= Lists.newArrayList();
        for(StatusEnum statusEnum: StatusEnum.values()){
            values.add(statusEnum.toString());
        }
        return values;
    }
}
