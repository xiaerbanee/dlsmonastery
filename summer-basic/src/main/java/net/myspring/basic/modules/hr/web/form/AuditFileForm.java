package net.myspring.basic.modules.hr.web.form;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.basic.modules.sys.domain.ProcessType;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/6.
 */

public class AuditFileForm extends DataForm<AuditFile> {
    private String processTypeId;
    private Folder folder;
    private List<ProcessType> processTypesList= Lists.newArrayList();
    private Map<Boolean, String> boolMap= Maps.newHashMap();

    public void setBoolMap(Map<Boolean, String> boolMap) {
        this.boolMap = boolMap;
    }

    public Map<Boolean, String> getBoolMap() {
        return boolMap;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public List<ProcessType> getProcessTypesList() {
        return processTypesList;
    }

    public void setProcessTypesList(List<ProcessType> processTypesList) {
        this.processTypesList = processTypesList;
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }
}
