package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Chain;

import java.util.List;

/**
 * Created by lihx on 2017/4/18.
 */
public class ChainForm extends DataForm<Chain> {
    private String name;
    private List<String> depotIdList=Lists.newArrayList();

    public List<String> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<String> depotIdList) {
        this.depotIdList = depotIdList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
