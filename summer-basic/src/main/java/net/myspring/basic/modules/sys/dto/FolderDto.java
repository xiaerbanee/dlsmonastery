package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.basic.modules.sys.domain.FolderFile;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class FolderDto  extends DataDto<Folder> {
    private String name;
    private String levelName;
    private String ParentId;
    private String ParentIds;
    private boolean locked;
    private boolean enabled;
    private List<FolderFile> folderFileList = Lists.newArrayList();
    private List<String> folderFileIdList = Lists.newArrayList();

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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

    public List<FolderFile> getFolderFileList() {
        return folderFileList;
    }

    public void setFolderFileList(List<FolderFile> folderFileList) {
        this.folderFileList = folderFileList;
    }

    public List<String> getFolderFileIdList() {
        return folderFileIdList;
    }

    public void setFolderFileIdList(List<String> folderFileIdList) {
        this.folderFileIdList = folderFileIdList;
    }
}
