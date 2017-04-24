package net.myspring.basic.common.enums;

/**
 * Created by wangzm on 2017/4/24.
 */
public enum OfficeRuleEnum {
    业务部门("businessOffice",100),
    后勤部门("rearOffice",200);

    private String code;
    private Integer type;

    OfficeRuleEnum(String code,Integer type){
        this.type=type;
        this.code=code;
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
