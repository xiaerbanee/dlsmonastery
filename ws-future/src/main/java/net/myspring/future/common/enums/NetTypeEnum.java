package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by admin on 2017/2/18.
 */
public enum NetTypeEnum {
    全网通, 移动, 联信;
    /*NETMOSA,CMCC,CHUCHA*/
    private static List<String> list= Lists.newArrayList();

    public static List<String> getList(){
        if(CollectionUtil.isEmpty(list)){
            for(NetTypeEnum netTypeEnum:NetTypeEnum.values()){
                list.add(netTypeEnum.name());
            }
        }
        return list;
    }

}
