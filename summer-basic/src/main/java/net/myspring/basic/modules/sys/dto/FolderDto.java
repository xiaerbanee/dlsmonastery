package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.Folder;

/**
 * Created by admin on 2017/4/5.
 */
public class FolderDto  extends DataDto<Folder> {
    private String name;
    private String levelName;
    private String ParentId;
    private String ParentIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getParentIds() {
        return ParentIds;
    }

    public void setParentIds(String parentIds) {
        ParentIds = parentIds;
    }
}
