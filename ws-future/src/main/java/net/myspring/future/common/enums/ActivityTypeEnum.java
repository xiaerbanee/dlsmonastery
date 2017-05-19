package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by zhangyf on 2017/5/4.
 */
public enum ActivityTypeEnum {
    路演,拉销;
    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(ActivityTypeEnum activityTypeEnum:ActivityTypeEnum.values()){
                list.add(activityTypeEnum.name());
            }
        }
        return list;
    }

}
