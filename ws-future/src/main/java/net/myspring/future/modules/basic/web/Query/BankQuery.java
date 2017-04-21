package net.myspring.future.modules.basic.web.Query;

import com.google.common.collect.Lists;
import org.elasticsearch.license.LicensesStatus;

import java.util.List;

/**
 * Created by lihx on 2017/4/18.
 */
public class BankQuery {

    private String name;
    private List<String> depotIdList= Lists.newArrayList();
    private List<String> officeIdList=Lists.newArrayList();

    public List<String> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<String> depotIdList) {
        this.depotIdList = depotIdList;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
