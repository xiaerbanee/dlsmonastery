package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.domain.Depot;

import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class ChainDto extends DataDto<Chain>{
    private String name;
    private Integer version;
    private List<String> accountIdList = Lists.newArrayList();
    private List<Depot> depotList = Lists.newArrayList();
    private List<String> depotIdList = Lists.newArrayList();
    private List<String> newDepotIdList = Lists.newArrayList();
    private List<String> pageIds = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
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

    public List<String> getNewDepotIdList() {
        return newDepotIdList;
    }

    public void setNewDepotIdList(List<String> newDepotIdList) {
        this.newDepotIdList = newDepotIdList;
    }

    public List<String> getPageIds() {
        return pageIds;
    }

    public void setPageIds(List<String> pageIds) {
        this.pageIds = pageIds;
    }
}
