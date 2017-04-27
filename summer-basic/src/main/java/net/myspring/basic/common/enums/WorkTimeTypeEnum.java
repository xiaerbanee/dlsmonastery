package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by admin on 2016/12/31.
 */
public enum WorkTimeTypeEnum {
    PUNCH_THE_CLOCK,
    WORK_DONE_OUTSIDE,
    FILL_THE_CLOCK;

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
