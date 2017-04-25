package net.myspring.general.modules.sys.form;

/**
 * Created by wangzm on 2017/4/25.
 */
public class ActivitiAuthenticatedForm {
    private String id;
    private String name;
    private String key;
    private String processTypeId;

    public ActivitiAuthenticatedForm(){};
    public ActivitiAuthenticatedForm(String id,String name,String key,String processTypeId){
        this.id=id;
        this.name=name;
        this.key=key;
        this.processTypeId=processTypeId;
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
