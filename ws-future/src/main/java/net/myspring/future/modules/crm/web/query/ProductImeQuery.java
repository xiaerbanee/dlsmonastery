package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by haos on 2017/5/12.
 */
public class ProductImeQuery extends BaseQuery{
    private List<String> officeIdList;
    private List<String> depotIdList;

    private String ime;
    private String imeReverse;
    private String boxIme;
    private String ime2;
    private String meid;
    private String depotId;
    private String imes;
    private String productId;
    private String createdDate;
    private String retailDate;
    private String createTime;
    private String inputType;

    //分页数据
    private Pageable pageable;

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public List<String> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<String> depotIdList) {
        this.depotIdList = depotIdList;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
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


    public String getImes() {
        return imes;
    }

    public void setImes(String imes) {
        this.imes = imes;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getRetailDate() {
        return retailDate;
    }

    public void setRetailDate(String retailDate) {
        this.retailDate = retailDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public List<String> getImeList() {
        if(StringUtils.isNotBlank(imes)) {
            return StringUtils.getSplitList(imes, CharConstant.ENTER);
        } else {
            return null;
        }
    }

    public LocalDate getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getRetailDateStart() {
        if(StringUtils.isNotBlank(retailDate)) {
            return LocalDateUtils.parse(retailDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getRetailDateEnd() {
        if(StringUtils.isNotBlank(retailDate)) {
            return LocalDateUtils.parse(retailDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }

    public LocalDate getCreateTimeStart() {
        if(StringUtils.isNotBlank(createTime)) {
            return LocalDateUtils.parse(createTime.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return null;
        }
    }

    public LocalDate getCreateTimeEnd() {
        if(StringUtils.isNotBlank(createTime)) {
            return LocalDateUtils.parse(createTime.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return null;
        }
    }
}