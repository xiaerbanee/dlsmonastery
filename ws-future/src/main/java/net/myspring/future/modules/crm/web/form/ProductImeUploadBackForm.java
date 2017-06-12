package net.myspring.future.modules.crm.web.form;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.util.text.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ProductImeUploadBackForm extends BaseForm<ProductImeUpload> {

    private String imeStr;

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
}
