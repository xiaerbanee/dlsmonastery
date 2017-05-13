package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Maps;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by lihx on 2017/4/17.
 */
public class DepotDto extends DataDto<Depot> {
    private String code;
    // 名称
    private String name;
    // 拼音
    private String namePinyin;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getFullName() {
        return namePinyin + CharConstant.SPACE + name + CharConstant.SPACE + code;
    }
}
