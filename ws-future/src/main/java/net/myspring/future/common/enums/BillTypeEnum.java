package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by lihx on 2017/4/15.
 */
public enum BillTypeEnum {
    柜台, 配件赠品, POP;

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(BillTypeEnum BillTypeEnum:BillTypeEnum.values()){
                list.add(BillTypeEnum.name());
            }
        }
        return list;
    }
}
