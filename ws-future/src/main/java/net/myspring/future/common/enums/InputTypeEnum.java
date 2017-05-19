package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by haos on 2017/5/12.
 */
public enum InputTypeEnum {

    工厂入库, 手工入库;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(InputTypeEnum each : InputTypeEnum.values()){
                list.add(each.name());
            }
        }
        return list;
    }
}
