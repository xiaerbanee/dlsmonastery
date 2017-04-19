package net.myspring.basic.modules.sys.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import net.myspring.common.domain.DataEntity;
import net.myspring.util.text.StringUtils;

@Entity
@Table(name="sys_frontend_menu")
public class FrontendMenu extends DataEntity<FrontendMenu> {
    private Integer version = 0;
    private Frontend frontend;
    private String frontendId;
    private Menu menu;
    private String menuId;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Frontend getFrontend() {
        return frontend;
    }

    public void setFrontend(Frontend frontend) {
        this.frontend = frontend;
    }

    public String getFrontendId() {
        if(StringUtils.isBlank(frontendId) && frontend!=null) {
            frontendId = frontend.getId();
        }
        return frontendId;
    }

    public void setFrontendId(String frontendId) {
        this.frontendId = frontendId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getMenuId() {
        if(StringUtils.isBlank(menuId) && menu!=null) {
            menuId = menu.getId();
        }
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
