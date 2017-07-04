package net.myspring.tool.modules.sys.domain;

import net.myspring.tool.common.domain.CompanyEntity;
import net.myspring.tool.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liuj on 2017/4/5.
 */
@Entity
@Table(name="sys_factory")
public class Factory extends CompanyEntity<Factory> {
    private String name;
    private String url;
    private String username;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
