package net.myspring.general.modules.sys.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.general.modules.sys.domain.Folder;
import net.myspring.general.modules.sys.dto.FolderDto;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class FolderForm extends BaseForm<Folder> {
    private String name;
    private String parentIds;
    private String parentId;
    private String remarks;

    private List<FolderDto> folderList = Lists.newArrayList();


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<FolderDto> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<FolderDto> folderList) {
        this.folderList = folderList;
    }
}
