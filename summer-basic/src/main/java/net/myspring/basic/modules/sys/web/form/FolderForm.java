package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.mybatis.annotation.FormDomain;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
@FormDomain(Folder.class)
public class FolderForm {

    private Folder parent;

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }
}
