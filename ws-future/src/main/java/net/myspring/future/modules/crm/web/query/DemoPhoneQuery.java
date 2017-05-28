package net.myspring.future.modules.crm.web.query;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;

/**
 * Created by sungm on 2017/5/22.
 */
public class DemoPhoneQuery extends BaseQuery {
    private String ime;
    private String shopId;
    private String demoPhoneTypeId;
    private String createdDate;

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

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return createdDate;
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
}
