package net.myspring.general.modules.sys.form;

/**
 * Created by wangzm on 2017/4/26.
 */
public class ActivitiNotifyForm {

    private String name;
    private String extendId;
    private String positionId;
    private String processStatus;
    private String officeId;
    private String accountId;
    private String companyId;

    public ActivitiNotifyForm(){};
    public ActivitiNotifyForm(String name,String extendId,String processStatus,String positionId,String companyId,String accountId,String officeId){
        this.extendId=extendId;
        this.name=name;
        this.officeId=officeId;
        this.accountId=accountId;
        this.companyId=companyId;
        this.positionId=positionId;
        this.processStatus=processStatus;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }
}
