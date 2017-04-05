package net.myspring.basic.modules.sys.domain;

import com.google.common.collect.Lists;
import net.myspring.common.domain.TreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="sys_folder")
public class Folder extends TreeEntity<Folder> {
    private String name;
    private Integer version = 0;
    private String companyId = "1";
    private List<FolderFile> folderFileList = Lists.newArrayList();
    private List<String> folderFileIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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
