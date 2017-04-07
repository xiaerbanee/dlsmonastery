package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.Folder;

/**
 * Created by admin on 2017/4/5.
 */
public class FolderDto  extends DataDto<Folder> {

    private String levelName;

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
