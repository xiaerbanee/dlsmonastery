package net.myspring.basic.modules.sys.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.myspring.basic.modules.hr.domain.PositionModule;
import net.myspring.common.domain.CompanyEntity;

import java.util.List;

import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

@Entity
@Table(name = "sys_backend")
public class Backend extends DataEntity<Backend> {
    private String name;
    private Integer version = 0;
    private List<BackendModule> backendModuleList = Lists.newArrayList();
    private List<String> backendModuleIdList = Lists.newArrayList();
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

    public List<BackendModule> getBackendModuleList() {
        return backendModuleList;
    }

    public void setBackendModuleList(List<BackendModule> backendModuleList) {
        this.backendModuleList = backendModuleList;
    }

    public List<String> getBackendModuleIdList() {
        return backendModuleIdList;
    }

    public void setBackendModuleIdList(List<String> backendModuleIdList) {
        this.backendModuleIdList = backendModuleIdList;
    }
}
