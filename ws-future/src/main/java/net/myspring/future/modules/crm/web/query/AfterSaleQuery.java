package net.myspring.future.modules.crm.web.query;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by wangzm on 2017/5/12.
 */
public class AfterSaleQuery {

    private String imeStr;
    private String type;
    private String toCompanyDateRanger;
    private String productTypeId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImeStr() {
        return imeStr;
    }

    public void setImeStr(String imeStr) {
        this.imeStr = imeStr;
    }

    public String getToCompanyDateRanger() {
        return toCompanyDateRanger;
    }

    public void setToCompanyDateRanger(String toCompanyDateRanger) {
        this.toCompanyDateRanger = toCompanyDateRanger;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public LocalDate getToCompanyDateStart() {
        if(StringUtils.isNotBlank(toCompanyDateRanger)) {
            return LocalDateUtils.parse(toCompanyDateRanger.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getToCompanyDateEnd() {
        if(StringUtils.isNotBlank(toCompanyDateRanger)) {
            return LocalDateUtils.parse(toCompanyDateRanger.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public List<String> getImeList(){
        List<String> imeList= Lists.newArrayList();
        if(StringUtils.isNotBlank(imeStr)){
            imeList=StringUtils.getSplitList(imeStr,CharConstant.ENTER);
        }
        return imeList;
    }
}
