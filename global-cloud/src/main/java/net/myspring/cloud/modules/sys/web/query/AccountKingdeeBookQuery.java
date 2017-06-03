package net.myspring.cloud.modules.sys.web.query;

import java.util.List;

/**
 * Created by lihx on 2017/5/10.
 */
public class AccountKingdeeBookQuery {
    private String accountId;
    private String username;
    private String kingdeeBookName;
    private String kingdeeBookType;
    private String companyId;
    private List<String> kingdeeBookTypeList;
    private List<String> kingdeeBookNameList;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKingdeeBookName() {
        return kingdeeBookName;
    }

    public void setKingdeeBookName(String kingdeeBookName) {
        this.kingdeeBookName = kingdeeBookName;
    }

    public String getKingdeeBookType() {
        return kingdeeBookType;
    }

    public void setKingdeeBookType(String kingdeeBookType) {
        this.kingdeeBookType = kingdeeBookType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<String> getKingdeeBookTypeList() {
        return kingdeeBookTypeList;
    }

    public void setKingdeeBookTypeList(List<String> kingdeeBookTypeList) {
        this.kingdeeBookTypeList = kingdeeBookTypeList;
    }

    public List<String> getKingdeeBookNameList() {
        return kingdeeBookNameList;
    }

    public void setKingdeeBookNameList(List<String> kingdeeBookNameList) {
        this.kingdeeBookNameList = kingdeeBookNameList;
    }
}
