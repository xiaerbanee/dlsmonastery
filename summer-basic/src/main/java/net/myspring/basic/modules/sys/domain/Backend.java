package net.myspring.basic.modules.sys.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.myspring.basic.modules.hr.domain.PositionModule;
import net.myspring.common.domain.CompanyEntity;

import java.util.List;

import com.google.common.collect.Lists;

@Entity
@Table(name = "sys_backend")
public class Backend extends CompanyEntity<Backend> {
    private String name;
    private Integer version = 0;
    private List<PositionModule> positionBackendList = Lists.newArrayList();
    private List<String> positionBackendIdList = Lists.newArrayList();
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

    public List<PositionModule> getPositionBackendList() {
        return positionBackendList;
    }

    public void setPositionBackendList(List<PositionModule> positionBackendList) {
        this.positionBackendList = positionBackendList;
    }

    public List<String> getPositionBackendIdList() {
        return positionBackendIdList;
    }

    public void setPositionBackendIdList(List<String> positionBackendIdList) {
        this.positionBackendIdList = positionBackendIdList;
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
