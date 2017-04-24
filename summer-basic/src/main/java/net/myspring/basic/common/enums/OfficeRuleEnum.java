package net.myspring.basic.common.enums;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/24.
 */
public enum OfficeRuleEnum {
    业务部门("businessOffice",100),
    后勤部门("rearOffice",200);

    private static List<OfficeRuleEnum> officeRuleEnumList= Lists.newArrayList();
    private static Map<Integer,String> map= Maps.newHashMap();
    private String code;
    private Integer type;

    OfficeRuleEnum(String code,Integer type){
        this.type=type;
        this.code=code;
    }

    public static List<OfficeRuleEnum> getOfficeRuleEnumList(){
        if(CollectionUtil.isEmpty(officeRuleEnumList)){
            for(OfficeRuleEnum officeRuleEnum:OfficeRuleEnum.values()){
                officeRuleEnumList.add(officeRuleEnum);
            }
        }
        return officeRuleEnumList;
    }

    public static Map<Integer,String> getMap(){
        if(CollectionUtil.isEmpty(map)){
            for(OfficeRuleEnum officeRuleEnum:OfficeRuleEnum.values()){
                map.put(officeRuleEnum.type,officeRuleEnum.name());
            }
        }
        return map;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue(){
        return name();
    }
}
