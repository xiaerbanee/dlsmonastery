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
    private String inputDateRanger;
    private String productTypeId;
    private String badDepotName;
    private String replaceProductImeStr;
    private String detailRemarks;

    public String getBadDepotName() {
        return badDepotName;
    }

    public void setBadDepotName(String badDepotName) {
        this.badDepotName = badDepotName;
    }

    public String getReplaceProductImeStr() {
        return replaceProductImeStr;
    }

    public void setReplaceProductImeStr(String replaceProductImeStr) {
        this.replaceProductImeStr = replaceProductImeStr;
    }

    public String getDetailRemarks() {
        return detailRemarks;
    }

    public void setDetailRemarks(String detailRemarks) {
        this.detailRemarks = detailRemarks;
    }

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

    public String getInputDateRanger() {
        return inputDateRanger;
    }

    public void setInputDateRanger(String inputDateRanger) {
        this.inputDateRanger = inputDateRanger;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public LocalDate getInputDateStart() {
        if(StringUtils.isNotBlank(inputDateRanger)) {
            return LocalDateUtils.parse(inputDateRanger.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getInputDateEnd() {
        if(StringUtils.isNotBlank(inputDateRanger)) {
            return LocalDateUtils.parse(inputDateRanger.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
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

    public List<String> getReplaceProductImeList(){
        List<String> imeList= Lists.newArrayList();
        if(StringUtils.isNotBlank(replaceProductImeStr)){
            imeList=StringUtils.getSplitList(replaceProductImeStr,CharConstant.ENTER);
        }
        return imeList;
    }
}
