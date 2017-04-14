package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.District;
import net.myspring.basic.modules.sys.domain.FolderFile;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;
import org.springframework.security.access.method.P;

/**
 * Created by admin on 2017/4/5.
 */
public class FolderFileDto extends DataDto<FolderFile> {
    private String folderId;
    private String contentType;
    private Integer size;
    private String physicalName;
    private String name;

    @CacheInput(inputKey = "folders",inputInstance = "folderId",outputInstance = "name")
    private String folderName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtendType() {
        if(StringUtils.isNotBlank(getName())){
            return getName().substring(getName().lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }

    public Boolean isImage() {
        String extend = getExtendType();
        return "jpg".equals(extend) || "jpeg".equals(extend) || "gif".equals(extend) || "png".equals(extend) || "bmp".equals(extend);
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getPhysicalName() {
        return physicalName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
