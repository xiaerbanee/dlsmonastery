package net.myspring.general.modules.sys.form;

/**
 * Created by wangzm on 2017/4/25.
 */
public class ActivitiAuthenticatedForm {
    private String id;
    private String name;
    private String key;
    private String accountId;
    private String processTypeId;
    private String officeId;
    private String companyId;

    public ActivitiAuthenticatedForm(){};
    public ActivitiAuthenticatedForm(String id,String name,String key,String processTypeId,String accountId,String officeId,String companyId){
        this.id=id;
        this.name=name;
        this.officeId=officeId;
        this.key=key;
        this.companyId=companyId;
        this.processTypeId=processTypeId;
        this.accountId=accountId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }
}
