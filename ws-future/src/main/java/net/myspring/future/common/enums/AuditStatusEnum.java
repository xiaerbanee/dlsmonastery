package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum AuditStatusEnum {
        申请中, 已通过, 未通过;
        /*APPLY, PASSED, NOTPASS;*/

        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
                if(CollectionUtil.isEmpty(list)){
                        for(AuditStatusEnum each :AuditStatusEnum.values()){
                                list.add(each.name());
                        }
                }
                return list;
        }

}
