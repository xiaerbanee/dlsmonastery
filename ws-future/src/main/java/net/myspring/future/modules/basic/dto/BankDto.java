package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class BankDto extends DataDto<Bank> {

    private String name;
    private String code;
    private List<String> accountIdList= Lists.newArrayList();
    private Boolean enabled;
    private String accountIdStr;
    @CacheInput(inputKey = "accounts",inputInstance = "accountIdList",outputInstance = "loginName")
    private List<String> accountNameList=Lists.newArrayList();

    public String getAccountIdStr() {
        return accountIdStr;
    }

    public void setAccountIdStr(String accountIdStr) {
        this.accountIdStr = accountIdStr;
    }

    public List<String> getAccountNameList() {
        return accountNameList;
    }

    public void setAccountNameList(List<String> accountNameList) {
        this.accountNameList = accountNameList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getAccountIdList() {
        if(CollectionUtil.isEmpty(accountIdList)&&StringUtils.isNotBlank(accountIdStr)){
            accountIdList=StringUtils.getSplitList(accountIdStr,CharConstant.COMMA);
        }
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getAccountNameStr(){
        if(CollectionUtil.isNotEmpty(accountNameList)){
            return StringUtils.join(accountNameList, CharConstant.COMMA);
        }
        return "";
    }
}
