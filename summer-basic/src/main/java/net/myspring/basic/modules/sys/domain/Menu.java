package net.myspring.basic.modules.sys.domain;

import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_menu")
public class Menu extends DataEntity<Menu> {
    private String name;
    private Integer sort;
    private Boolean mobile;
    private Integer version = 0;
    private Boolean visible;
    private String mobileHref;
    private String mobileIcon;
    private String menuCategoryId;
    private String code;

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getMobileHref() {
        return mobileHref;
    }

    public void setMobileHref(String mobileHref) {
        this.mobileHref = mobileHref;
    }

    public String getMobileIcon() {
        return mobileIcon;
    }

    public void setMobileIcon(String mobileIcon) {
        this.mobileIcon = mobileIcon;
    }

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }
}
