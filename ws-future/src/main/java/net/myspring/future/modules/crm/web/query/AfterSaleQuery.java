package net.myspring.future.modules.crm.web.query;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Created by wangzm on 2017/5/12.
 */
public class AfterSaleQuery  extends BaseQuery{
    private String badProductName;
    private String badImeStr;
    private String businessIdStr;
    private String toAreaImeStr;
    private String toStoreDateRange;
    private String toCompanyDateRange;
    private String fromCompanyDateRange;
    private String createdRange;
    private String depotName;
    private List<String> imeList=Lists.newArrayList();
    private List<String> toAreaImeList=Lists.newArrayList();
    private List<String> businessIdList=Lists.newArrayList();

    private boolean fromCompany=false;

    public boolean getFromCompany() {
        return this.fromCompany;
    }

    public void setFromCompany(boolean fromCompany) {
        this.fromCompany = fromCompany;
    }

    public String getBusinessIdStr() {
        return businessIdStr;
    }

    public void setBusinessIdStr(String businessIdStr) {
        this.businessIdStr = businessIdStr;
    }

    public List<String> getBusinessIdList() {
        if(StringUtils.isNotBlank(businessIdStr)){
            List<String> formatIdList=StringUtils.getFilterList(businessIdStr);
            businessIdList=Lists.newArrayList();
            for(String formatId:formatIdList){
                businessIdList.add(formatId.replace("XK",""));
            }
        }
        return businessIdList;
    }

    public void setBusinessIdList(List<String> businessIdList) {
        this.businessIdList = businessIdList;
    }


    public String getToAreaImeStr() {
        return toAreaImeStr;
    }

    public void setToAreaImeStr(String toAreaImeStr) {
        this.toAreaImeStr = toAreaImeStr;
    }

    public List<String> getToAreaImeList() {
        if(CollectionUtil.isEmpty(toAreaImeList)&&StringUtils.isNotBlank(toAreaImeStr)){
            this.toAreaImeList=StringUtils.getFilterList(toAreaImeStr);
        }
        return toAreaImeList;
    }

    public void setToAreaImeList(List<String> toAreaImeList) {
        this.toAreaImeList = toAreaImeList;
    }

    public String getBadProductName() {
        return badProductName;
    }

    public void setBadProductName(String badProductName) {
        this.badProductName = badProductName;
    }

    public String getBadImeStr() {
        return badImeStr;
    }

    public void setBadImeStr(String badImeStr) {
        this.badImeStr = badImeStr;
    }

    public String getToStoreDateRange() {
        return toStoreDateRange;
    }

    public void setToStoreDateRange(String toStoreDateRange) {
        this.toStoreDateRange = toStoreDateRange;
    }

    public String getToCompanyDateRange() {
        return toCompanyDateRange;
    }

    public void setToCompanyDateRange(String toCompanyDateRange) {
        this.toCompanyDateRange = toCompanyDateRange;
    }

    public String getFromCompanyDateRange() {
        return fromCompanyDateRange;
    }

    public void setFromCompanyDateRange(String fromCompanyDateRange) {
        this.fromCompanyDateRange = fromCompanyDateRange;
    }

    public String getCreatedRange() {
        return createdRange;
    }

    public void setCreatedRange(String createdRange) {
        this.createdRange = createdRange;
    }


    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }


    public List<String> getImeList() {
        if(CollectionUtil.isEmpty(imeList)&&StringUtils.isNotBlank(badImeStr)){
            this.imeList=StringUtils.getFilterList(badImeStr);
        }
        return imeList;
    }

    public void setImeList(List<String> imeList) {
        this.imeList = imeList;
    }

    public LocalDate getToStoreDateStart() {
        if(StringUtils.isNotBlank(toStoreDateRange)) {
            return LocalDateUtils.parse(toStoreDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getToStoreDateEnd() {
        if(StringUtils.isNotBlank(toStoreDateRange)) {
            return LocalDateUtils.parse(toStoreDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getToCompanyDateStart() {
        if(StringUtils.isNotBlank(toCompanyDateRange)) {
            return LocalDateUtils.parse(toCompanyDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getToCompanyDateEnd() {
        if(StringUtils.isNotBlank(toCompanyDateRange)) {
            return LocalDateUtils.parse(toCompanyDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdRange)) {
            return LocalDateUtils.parse(createdRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdRange)) {
            return LocalDateUtils.parse(createdRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getFromCompanyDateStart() {
        if(StringUtils.isNotBlank(fromCompanyDateRange)) {
            return LocalDateUtils.parse(fromCompanyDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getFromCompanyDateEnd() {
        if(StringUtils.isNotBlank(fromCompanyDateRange)) {
            return LocalDateUtils.parse(fromCompanyDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }
}
