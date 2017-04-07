package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.mybatis.form.BaseForm;

/**
 * Created by admin on 2017/4/5.
 */

public class FolderForm implements BaseForm<Folder>{
    private String id;
    private Folder parent;
    private String name;
    private String parentIds;
    private String parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
