package net.myspring.basic.modules.hr.web.form;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.AccountChange;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.hr.manager.AccountManager;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/6.
 */
public class AccountChangeForm extends DataForm<AccountChange> {

    private String type;
    private String accountId;
    @CacheInput(inputKey = "accounts",inputInstance = "accountId",outputInstance = "loginName")
    private String accountName;
    private String remarks;
    private List<String> typeList= Lists.newArrayList();
    private List<PositionDto> positionList=Lists.newArrayList();

    private Map<String,String> olderValueMap= Maps.newHashMap();

    public Map<String, String> getOlderValueMap() {
        if(StringUtils.isNotBlank(accountId)){

        }
        return olderValueMap;
    }

    public void setOlderValueMap(Map<String, String> olderValueMap) {
        this.olderValueMap = olderValueMap;
    }

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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
