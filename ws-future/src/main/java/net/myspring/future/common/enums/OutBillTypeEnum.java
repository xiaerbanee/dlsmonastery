package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by lihx on 2017/4/15.
 */
public enum OutBillTypeEnum {
        其他应收单, 手工日记账,不同步到金蝶;
        /*OTHER_RECEIVE_ORDER,MANUAL_TALLY,OUTSYNC_KINGDEE*/

    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(OutBillTypeEnum each : OutBillTypeEnum.values()){
                list.add(each.name());
            }
        }
        return list;
    }

}
