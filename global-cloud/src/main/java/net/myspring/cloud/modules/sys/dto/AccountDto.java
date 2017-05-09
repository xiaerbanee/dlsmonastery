package net.myspring.cloud.modules.sys.dto;

import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

/**
 * Created by lihx on 2017/5/6.
 */
public class AccountDto {
    private String id = SecurityUtils.getAccountId();
    @CacheInput(inputKey = "accounts",inputInstance = "id",outputInstance = "loginName")
    private String name;
    @CacheInput(inputKey = "accounts",inputInstance = "id",outputInstance = "outId")
    private String outId;
    @CacheInput(inputKey = "accounts",inputInstance = "id",outputInstance = "outPassword")
    private String outPassword;

    public String getId() {
        if(StringUtils.isBlank(id)){
            return SecurityUtils.getAccountId();
        }
        return id;
    }

    public void setId(String id) {
        if(StringUtils.isBlank(id)){
            this.id = SecurityUtils.getAccountId();
        }else {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getOutPassword() {
        return outPassword;
    }

    public void setOutPassword(String outPassword) {
        this.outPassword = outPassword;
    }
}
