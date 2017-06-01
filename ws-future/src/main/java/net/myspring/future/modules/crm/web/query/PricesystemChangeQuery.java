package net.myspring.future.modules.crm.web.query;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.dto.PricesystemDto;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by haos on 2017/5/13.
 */
public class PricesystemChangeQuery extends BaseQuery {

    private String productId;
    private String createdDate;
    private String status;
    private String pricesystemId;
    private List<String> statusList= Lists.newArrayList();
    private List<PricesystemDto> pricesystems=Lists.newArrayList();

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
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

    public List<PricesystemDto> getPricesystems() {
        return pricesystems;
    }

    public void setPricesystems(List<PricesystemDto> pricesystems) {
        this.pricesystems = pricesystems;
    }
}
