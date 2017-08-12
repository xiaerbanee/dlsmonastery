package net.myspring.basic.modules.hr.web.form;

import net.myspring.basic.modules.hr.domain.AccountFavorite;
import net.myspring.common.form.BaseForm;

public class AccountFavoriteForm extends BaseForm<AccountFavorite> {

    private String accountId;
    private String name;
    private String parentId;
    private String parentIds;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
}
