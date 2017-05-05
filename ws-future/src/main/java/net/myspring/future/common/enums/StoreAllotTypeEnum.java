package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum StoreAllotTypeEnum {
        普通调拨,快速调拨;

        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
                if(CollectionUtil.isEmpty(list)){
                        for(StoreAllotTypeEnum each : StoreAllotTypeEnum.values()){
                                list.add(each.name());
                        }
                }
                return list;
        }

}
