package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.basic.common.form.DataForm;

/**
 * Created by admin on 2017/4/5.
 */

public class FolderForm extends DataForm<Folder>{
    private Folder parent;
    private String name;
    private String parentIds;
    private String parentId;

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
