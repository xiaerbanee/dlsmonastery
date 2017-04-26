package net.myspring.basic.modules.sys.domain;

import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "sys_backend")
public class Backend extends DataEntity<Backend> {
    private String name;
    private String code;
    private Integer version = 0;
    private List<String> backendModuleIdList = Lists.newArrayList();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public List<String> getBackendModuleIdList() {
        return backendModuleIdList;
    }

    public void setBackendModuleIdList(List<String> backendModuleIdList) {
        this.backendModuleIdList = backendModuleIdList;
    }
}
