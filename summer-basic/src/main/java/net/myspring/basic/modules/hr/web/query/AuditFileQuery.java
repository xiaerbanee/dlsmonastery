package net.myspring.basic.modules.hr.web.query;

import com.google.common.collect.Lists;
import net.myspring.basic.common.query.BaseQuery;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.bouncycastle.cert.ocsp.Req;
import sun.misc.Request;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class AuditFileQuery extends BaseQuery {
    private String id;
    private String positionId;
    private String auditType;
    private List<String> officeIds;
    private String createdDate;
    private String officeName;
    private String officeId;
    private String createdByName;
    private String stageName;
    private String processTypeId;
    private String content;
    private String title;
    private String processflowName;
    private LocalDate createdDateStart;
    private LocalDate createdDateEnd;
    private List<String> processTypeIdList=Lists.newArrayList();
    private List<String> processFlowIdList=Lists.newArrayList();

    public List<String> getProcessTypeIdList() {
        return processTypeIdList;
    }

    public void setProcessTypeIdList(List<String> processTypeIdList) {
        if(CollectionUtil.isNotEmpty(processTypeIdList)){
            processTypeIdList.add("0");
        }
        this.processTypeIdList = processTypeIdList;
    }

    public List<String> getProcessFlowIdList() {
        return processFlowIdList;
    }

    public void setProcessFlowIdList(List<String> processFlowIdList) {
        this.processFlowIdList = processFlowIdList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPositionId() {
        if(StringUtils.isBlank(getAuditType())||!"全部".equals(getAuditType())) {
            this.positionId= RequestUtils.getPositionId();
        }else {
            this.positionId=null;
        }
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getAuditType() {
        if(StringUtils.isBlank(auditType)) {
            this.auditType= "待批(需要我审核)";
        }
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcessflowName() {
        return processflowName;
    }

    public void setProcessflowName(String processflowName) {
        this.processflowName = processflowName;
    }

    public LocalDate getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else if(createdDateStart!=null){
            return createdDateStart;
        }
        return null;
    }

    public void setDutyDateStart(LocalDate dutyDateStart) {
        this.createdDateStart = dutyDateStart;
    }

    public LocalDate getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateUtils.parse(createdDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else if(createdDateEnd!=null){
            return createdDateEnd;
        }
        return null;
    }

    public void setDutyDateEnd(LocalDate dutyDateEnd) {
        this.createdDateEnd = dutyDateEnd;
    }
}
