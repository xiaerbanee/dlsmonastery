package net.myspring.future.modules.crm.web.form;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.util.text.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class ProductImeUploadForm extends BaseForm<ProductImeUpload> {

    private String imeStr;
    private String month;
    private String employeeId;
    private String shopId;


    public List<String> getImeList(){
        if(imeStr == null){
            return new ArrayList<>();
        }
        return  StringUtils.getSplitList(imeStr, CharConstant.ENTER);
    }

    public String getImeStr() {
        return imeStr;
    }

    public void setImeStr(String imeStr) {
        this.imeStr = imeStr;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
