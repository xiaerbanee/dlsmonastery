package net.myspring.general.modules.sys.form;

/**
 * Created by wangzm on 2017/4/25.
 */
public class ActivitiStartForm {
    private String name;
    private String businessKey;
    private String processTypeName;
    private String message;

    public ActivitiStartForm(){};
    public ActivitiStartForm(String name,String businessKey, String processTypeName,String message){
        this.name = name;
        this.message=message;
        this.businessKey=businessKey;
        this.processTypeName=processTypeName;
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

    public String getProcessTypeName() {
        return processTypeName;
    }

    public void setProcessTypeName(String processTypeName) {
        this.processTypeName = processTypeName;
    }
}
