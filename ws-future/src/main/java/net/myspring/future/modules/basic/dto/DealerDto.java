package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Dealer;
import net.myspring.future.modules.basic.domain.Depot;

import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class DealerDto extends DataDto<Dealer> {
    private String name;
    private String mobilePhone;
    private Integer version;
    private String accountId;
    private List<Depot> depotList = Lists.newArrayList();
    private List<String> depotIdList = Lists.newArrayList();
    private List<String> accountIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<Depot> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<Depot> depotList) {
        this.depotList = depotList;
    }

    public List<String> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<String> depotIdList) {
        this.depotIdList = depotIdList;
    }

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }
}
