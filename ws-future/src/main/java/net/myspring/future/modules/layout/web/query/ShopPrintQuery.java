package net.myspring.future.modules.layout.web.query;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.common.dto.NameValueDto;
import net.myspring.future.common.query.BaseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/5.
 */
public class ShopPrintQuery extends BaseQuery {

    private String areaId;
    private String printType;
    private String processStatus;
    private String createdBy;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
