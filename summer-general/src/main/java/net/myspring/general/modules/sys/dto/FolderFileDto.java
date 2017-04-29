package net.myspring.general.modules.sys.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.general.modules.sys.domain.Folder;
import net.myspring.general.modules.sys.domain.FolderFile;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

/**
 * Created by admin on 2017/4/5.
 */
public class FolderFileDto extends DataDto<FolderFile> {
    private String name;
    private String contentType;
    private Integer size;
    private String physicalName;
    private Integer version = 0;
    private String folderId;
    private String mongoId;
    private String mongoPreviewId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public String getMongoPreviewId() {
        return mongoPreviewId;
    }

    public void setMongoPreviewId(String mongoPreviewId) {
        this.mongoPreviewId = mongoPreviewId;
    }

    public String getExtendType() {
        if(StringUtils.isNotBlank(name)){
            return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }

    public Boolean isImage() {
        String extend = getExtendType();
        return "jpg".equals(extend) || "jpeg".equals(extend) || "gif".equals(extend) || "png".equals(extend) || "bmp".equals(extend);
    }

    public String getUrl() {
        String url="/api/general/sys/folderFile/download?id=";
        if(isImage()) {
            return url+getMongoPreviewId()+"&type=preview";
        }else {
            return url+getMongoId()+"&type=storage";
        }
    }
}
