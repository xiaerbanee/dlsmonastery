package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum ExpressOrderExtendTypeEnum {
        手机订单, 物料订单;

        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
                if(CollectionUtil.isEmpty(list)){
                        for(ExpressOrderExtendTypeEnum each : ExpressOrderExtendTypeEnum.values()){
                                list.add(each.name());
                        }
                }
                return list;
        }

}
