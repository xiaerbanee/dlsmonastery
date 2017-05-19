package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;


public class ProductImeUploadQuery extends BaseQuery{

    private String month;
    private String imeOrMeids;
    private String shopName;
    private String officeId;
    private String createdDateRange;


    //分页数据
    private Pageable pageable;

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getImeOrMeids() {
        return imeOrMeids;
    }

    public void setImeOrMeids(String imeOrMeids) {
        this.imeOrMeids = imeOrMeids;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public void setCreatedDateRange(String createdDateRange) {
        this.createdDateRange = createdDateRange;
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

}
