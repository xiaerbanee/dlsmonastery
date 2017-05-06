package net.myspring.general.modules.sys.form;

/**
 * Created by wangzm on 2017/4/25.
 */
public class ActivitiStartForm {
    private String name;
    private String businessKey;
    private String processTypeId;
    private String message;

    public ActivitiStartForm(){};
    public ActivitiStartForm(String name,String businessKey, String processTypeId,String message){
        this.name = name;
        this.message=message;
        this.businessKey=businessKey;
        this.processTypeId=processTypeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
