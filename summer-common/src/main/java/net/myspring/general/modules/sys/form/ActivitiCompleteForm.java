package net.myspring.general.modules.sys.form;

import org.springframework.util.StringUtils;

/**
 * Created by wangzm on 2017/4/25.
 */
public class ActivitiCompleteForm {
    private String processInstanceId;
    private String processTypeId;
    private String comment;
    private boolean pass;

    public ActivitiCompleteForm(){};
    public ActivitiCompleteForm(String processInstanceId, String processTypeId, String comment, boolean pass){
        this.pass=pass;
        this.processInstanceId=processInstanceId;
        this.processTypeId=processTypeId;
        this.comment=comment;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }

    public String getComment() {
        if(StringUtils.isEmpty(comment)){
            this.comment="";
        }
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean getPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }
}
