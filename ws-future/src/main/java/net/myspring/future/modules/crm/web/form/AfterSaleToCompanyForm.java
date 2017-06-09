package net.myspring.future.modules.crm.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

import java.time.LocalDate;
import java.util.List;

public class AfterSaleToCompanyForm extends BaseForm<AfterSaleToCompanyForm>{
    private String imeStr;
    private List<String> imeList= Lists.newArrayList();
    private String type;
    private LocalDate toCompanyDate;
    private String toCompanyRemarks;

    public List<String> getImeList() {
        if(CollectionUtil.isEmpty(imeList)&& StringUtils.isNotBlank(imeStr)){
            this.imeList=StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        }
        return imeList;
    }

    public void setImeList(List<String> imeList) {
        this.imeList = imeList;
    }

    public String getImeStr() {
        return imeStr;
    }

    public void setImeStr(String imeStr) {
        this.imeStr = imeStr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getToCompanyDate() {
        return toCompanyDate;
    }

    public void setToCompanyDate(LocalDate toCompanyDate) {
        this.toCompanyDate = toCompanyDate;
    }

    public String getToCompanyRemarks() {
        return toCompanyRemarks;
    }

    public void setToCompanyRemarks(String toCompanyRemarks) {
        this.toCompanyRemarks = toCompanyRemarks;
    }
}
