package net.myspring.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum  JointLevelEnum {
    一级,二级;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(JointLevelEnum jointLevelEnum:JointLevelEnum.values()){
                list.add(jointLevelEnum.name());
            }
        }
        return list;
    }
    }
