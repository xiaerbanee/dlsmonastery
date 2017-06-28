package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.DutyWorktime;
import net.myspring.common.form.BaseForm;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyWorktimeForm extends BaseForm<DutyWorktime> {
    private String folderFileId;
    private String yearMonth;

    public String getFolderFileId() {
        return folderFileId;
    }

    public void setFolderFileId(String folderFileId) {
        this.folderFileId = folderFileId;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
}
