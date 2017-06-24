package net.myspring.general.modules.sys.dto;

public class FolderFileFeignDto {
    private String id;
    private String name;
    private String contentType;
    private Integer size;
    private String physicalName;
    private String folderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getUploadPath(String companyName) {
        return "D:\\upload\\" + companyName+ "\\upload\\" + getPhysicalName();
    }

    public String getPreviewUploadPath(String companyName) {
        return "D:\\upload\\" + companyName + "\\convert\\" + getPhysicalName().substring(0, getPhysicalName().lastIndexOf(".")) + ".png";
    }
}
