package net.myspring.general.modules.sys.form;

/**
 * Created by wangzm on 2017/4/25.
 */
public class ActivitiStartForm {
    private String name;
    private String businessKey;
    private String processTypeId;

    public ActivitiStartForm(){};
    public ActivitiStartForm(String name,String businessKey, String processTypeId){
        this.name = name;
        this.businessKey=businessKey;
        this.processTypeId=processTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }
}
