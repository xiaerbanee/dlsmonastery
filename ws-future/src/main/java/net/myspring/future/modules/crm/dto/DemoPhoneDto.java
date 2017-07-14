package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.DemoPhone;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by sungm on 2017/5/22.
 */
public class DemoPhoneDto extends DataDto<DemoPhone> {
    private String ime;
    private String shopId;
    private String shopName;
    private String areaId;
    @CacheInput(inputKey = "offices",inputInstance = "areaId",outputInstance = "name")
    private String areaName;
    private String demoPhoneTypeId;
    @CacheInput(inputKey = "demoPhoneTypes",inputInstance = "demoPhoneTypeId",outputInstance = "name")
    private String demoPhoneType;
    private String employeeId;
    private String productImeId;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    private String status;
    private boolean locked;
    private boolean enabled;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDemoPhoneTypeId() {
        return demoPhoneTypeId;
    }

    public void setDemoPhoneTypeId(String demoPhoneTypeId) {
        this.demoPhoneTypeId = demoPhoneTypeId;
    }

    public String getDemoPhoneType() {
        return demoPhoneType;
    }

    public void setDemoPhoneType(String demoPhoneType) {
        this.demoPhoneType = demoPhoneType;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
