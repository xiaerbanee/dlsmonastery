package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by Lenovo on 2017/5/17.
 */
public enum  TownTypeEnum {
    A类乡镇,B类乡镇,C类乡镇;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(TownTypeEnum townTypeEnum:TownTypeEnum.values()){
                list.add(townTypeEnum.name());
            }
        }
        return list;
    }
}
