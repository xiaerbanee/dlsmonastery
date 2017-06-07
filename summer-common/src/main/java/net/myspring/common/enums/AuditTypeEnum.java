package net.myspring.common.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by liuj on 2016/12/31.
 */
public enum AuditTypeEnum {
    APPLY("申请中"), PASS("已通过"), NOT_PASS("未通过");
    private String value;

    AuditTypeEnum(String value) {
        this.value = value;
    }

    public static List<String> getValues() {
        List<String> values = Lists.newArrayList();
        for(AuditTypeEnum auditTypeEnum: AuditTypeEnum.values()) {
            values.add(auditTypeEnum.getValue());
        }
        return values;
    }

    public String getValue() {
        return value;
    }
}