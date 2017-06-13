package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by zhucc on 2017/6/9.
 */
public enum ReportTypeEnum {
    核销,库存;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(ReportTypeEnum reportTypeEnum:ReportTypeEnum.values()){
                list.add(reportTypeEnum.name());
            }
        }
        return list;
    }
}
