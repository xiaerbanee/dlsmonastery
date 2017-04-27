package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by admin on 2017/2/25.
 */
public enum JointTypeEnum {
    DIRECT_SHOP,PROXY_SHOP;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(JointTypeEnum jointTypeEnum:JointTypeEnum.values()){
                list.add(jointTypeEnum.name());
            }
        }
        return list;
    }
}
