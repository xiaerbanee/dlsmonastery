package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.Folder;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class FolderForm extends Folder {

    private Folder parent;

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }
}
