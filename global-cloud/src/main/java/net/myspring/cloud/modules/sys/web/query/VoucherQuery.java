package net.myspring.cloud.modules.sys.web.query;

import net.myspring.cloud.common.query.BaseQuery;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/4/10.
 */
public class VoucherQuery extends BaseQuery {
    private String id;
    private LocalDate fdate;
    private String status;
    private String createdBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getFdate() {
        return fdate;
    }

    public void setFdate(LocalDate fdate) {
        this.fdate = fdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
