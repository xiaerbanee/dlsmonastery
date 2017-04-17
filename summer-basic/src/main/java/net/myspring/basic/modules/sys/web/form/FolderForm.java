package net.myspring.basic.modules.sys.web.form;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.dto.FolderDto;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class FolderForm extends DataForm<Folder>{
    private Folder parent;
    private String name;
    private String parentIds;
    private String parentId;
    private List<FolderDto> folderList= Lists.newArrayList();

    public List<FolderDto> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<FolderDto> folderList) {
        this.folderList = folderList;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
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
}
