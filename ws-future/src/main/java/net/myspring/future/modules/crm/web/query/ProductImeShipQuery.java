package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by haos on 2017/5/12.
 */
public class ProductImeShipQuery extends BaseQuery{
    private String depotId;
    private List<String> boxImeList;
    private List<String> imeList;

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public List<String> getBoxImeList() {
        return boxImeList;
    }

    public void setBoxImeList(List<String> boxImeList) {
        this.boxImeList = boxImeList;
    }

    public List<String> getImeList() {
        return imeList;
    }

    public void setImeList(List<String> imeList) {
        this.imeList = imeList;
    }

    public Boolean boxImeAndIme() {
        if(CollectionUtil.isNotEmpty(boxImeList) && CollectionUtil.isNotEmpty(imeList)) {
            return true;
        } else {
            return false;
        }
    }
}
