package net.myspring.general.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.common.dto.DataDto;
import net.myspring.general.modules.sys.domain.Folder;
import net.myspring.general.modules.sys.domain.FolderFile;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class FolderDto  extends DataDto<Folder> {
    private String name;
    private String levelName;
    private String parentId;
    private String parentIds;

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
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
}
