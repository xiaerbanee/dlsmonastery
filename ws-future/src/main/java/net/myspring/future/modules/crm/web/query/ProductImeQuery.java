package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;


public class ProductImeQuery extends BaseQuery{

    private String imeReverse;
    private String boxIme;
    private String ime2;
    private String meid;
    private String depotId;
    private String productId;
    private String imeOrMeids;
    private String createdDateRange;
    private String retailDateRange;
    private String createTimeRange;
    private String inputType;

    private List<String> inputTypeList;


    public List<String> getInputTypeList() {
        return inputTypeList;
    }

    public void setInputTypeList(List<String> inputTypeList) {
        this.inputTypeList = inputTypeList;
    }

    public String getImeOrMeids() {
        return imeOrMeids;
    }

    public void setImeOrMeids(String imeOrMeids) {
        this.imeOrMeids = imeOrMeids;
    }

    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public void setCreatedDateRange(String createdDateRange) {
        this.createdDateRange = createdDateRange;
    }

    public String getRetailDateRange() {
        return retailDateRange;
    }

    public void setRetailDateRange(String retailDateRange) {
        this.retailDateRange = retailDateRange;
    }

    public String getCreateTimeRange() {
        return createTimeRange;
    }

    public void setCreateTimeRange(String createTimeRange) {
        this.createTimeRange = createTimeRange;
    }

    public String getImeReverse() {
        return imeReverse;
    }

    public void setImeReverse(String imeReverse) {
        this.imeReverse = imeReverse;
    }

    public String getBoxIme() {
        return boxIme;
    }

    public void setBoxIme(String boxIme) {
        this.boxIme = boxIme;
    }

    public String getIme2() {
        return ime2;
    }

    public void setIme2(String ime2) {
        this.ime2 = ime2;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public List<String> getImeOrMeidList() {
        if(StringUtils.isNotBlank(imeOrMeids)) {
            return StringUtils.getSplitList(imeOrMeids, CharConstant.ENTER);
        } else {
            return null;
        }
    }

    public LocalDate getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdDateRange)) {
            return LocalDateUtils.parse(createdDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDateRange)) {
            return LocalDateUtils.parse(createdDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getRetailDateStart() {
        if(StringUtils.isNotBlank(retailDateRange)) {
            return LocalDateUtils.parse(retailDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getRetailDateEnd() {
        if(StringUtils.isNotBlank(retailDateRange)) {
            return LocalDateUtils.parse(retailDateRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getCreateTimeStart() {
        if(StringUtils.isNotBlank(createTimeRange)) {
            return LocalDateUtils.parse(createTimeRange.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getCreateTimeEnd() {
        if(StringUtils.isNotBlank(createTimeRange)) {
            return LocalDateUtils.parse(createTimeRange.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }
}
