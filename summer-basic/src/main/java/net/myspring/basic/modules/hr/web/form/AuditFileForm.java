package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.common.form.DataForm;

/**
 * Created by admin on 2017/4/6.
 */

public class AuditFileForm extends DataForm<AuditFile> {
    private String processTypeId;
    private String title;
    private String content;
    private String remarks;
    private String attachment;


    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
