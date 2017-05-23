package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto;
import net.myspring.future.modules.crm.dto.ProductImeDto;

import java.util.List;

/**
 * Created by sungm on 2017/5/22.
 */
public class DemoPhoneForm extends DataForm<DataForm>{
    private String demoPhoneTypeId;
    private String shopId;
    private String employeeId;
    private String productImeId;
    private List<DemoPhoneTypeDto> demoPhoneTypeList;
    private List<ProductImeDto> productImeDtoList;

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public List<ProductImeDto> getProductImeDtoList() {
        return productImeDtoList;
    }

    public void setProductImeDtoList(List<ProductImeDto> productImeDtoList) {
        this.productImeDtoList = productImeDtoList;
    }

    public String getDemoPhoneTypeId() {
        return demoPhoneTypeId;
    }

    public void setDemoPhoneTypeId(String demoPhoneTypeId) {
        this.demoPhoneTypeId = demoPhoneTypeId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<DemoPhoneTypeDto> getDemoPhoneTypeList() {
        return demoPhoneTypeList;
    }

    public void setDemoPhoneTypeList(List<DemoPhoneTypeDto> demoPhoneTypeList) {
        this.demoPhoneTypeList = demoPhoneTypeList;
    }
}
