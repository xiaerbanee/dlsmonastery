package net.myspring.basic.modules.hr.web.form;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.AccountChange;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.hr.dto.PositionDto;

import java.util.List;

/**
 * Created by admin on 2017/4/6.
 */
public class AccountChangeForm extends DataForm<AccountChange> {
    private String type;
    private String accountId;
    private String value;
    private String remarks;
    private List<String> typeList= Lists.newArrayList();
    private List<PositionDto> positionList=Lists.newArrayList();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public List<PositionDto> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<PositionDto> positionList) {
        this.positionList = positionList;
    }
}
