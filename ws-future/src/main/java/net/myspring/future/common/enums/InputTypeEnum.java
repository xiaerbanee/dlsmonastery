package net.myspring.future.common.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by haos on 2017/5/12.
 */
public enum InputTypeEnum {

    工厂入库, 手工入库;
    /*NUMBER,AREA*/

    public static List<String> getValues(){
        List<String> values= Lists.newArrayList();
        for(InputTypeEnum inputTypeEnum: InputTypeEnum.values()){
            values.add(inputTypeEnum.toString());
        }
        return values;
    }
}
