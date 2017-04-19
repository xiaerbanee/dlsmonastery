package net.myspring.basic.modules.sys.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import net.myspring.common.domain.DataEntity;
import java.util.List;
import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

@Entity
@Table(name="sys_frontend")
public class Frontend extends DataEntity<Frontend> {
    private String name;
    private Integer version = 0;
    private List<FrontendMenu> frontendMenuList = Lists.newArrayList();
    private List<String> frontendMenuIdList = Lists.newArrayList();

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

    public List<FrontendMenu> getFrontendMenuList() {
        return frontendMenuList;
    }

    public void setFrontendMenuList(List<FrontendMenu> frontendMenuList) {
        this.frontendMenuList = frontendMenuList;
    }

    public List<String> getFrontendMenuIdList() {
        if(CollectionUtil.isEmpty(frontendMenuIdList) && CollectionUtil.isNotEmpty(frontendMenuList)) {
            frontendMenuIdList = CollectionUtil.extractToList(frontendMenuList,"id");
        }
        return frontendMenuIdList;
    }

    public void setFrontendMenuIdList(List<String> frontendMenuIdList) {
        this.frontendMenuIdList = frontendMenuIdList;
    }
}
