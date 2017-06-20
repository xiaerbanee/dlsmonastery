package net.myspring.cloud.modules.sys.web.query;


import net.myspring.cloud.common.query.BaseQuery;

/**
 * Created by lihx on 2017/5/10.
 */
public class AccountKingdeeBookQuery extends BaseQuery {
    private String accountId;
    private String username;
    private String kingdeeBookName;
    private String kingdeeBookType;

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
}
