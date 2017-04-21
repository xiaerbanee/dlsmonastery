package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.future.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Bank;

import java.util.List;

/**
 * Created by lihx on 2017/4/18.
 */
public class BankForm extends DataForm<Bank> {
    private String name;
    private List<String> accountIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }
}
