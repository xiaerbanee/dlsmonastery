package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.myspring.common.dto.NameValueDto;
import net.myspring.future.common.query.BaseQuery;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.domain.Pricesystem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/18.
 */
public class DepotQuery extends BaseQuery {
    private String name;
    private List<String> officeIdList= Lists.newArrayList();
    private List<String> depotIdList= Lists.newArrayList();
    //直营，代理
    private Boolean clientIsNull;
    //广告门店
    private Boolean adShop;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public List<String> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<String> depotIdList) {
        this.depotIdList = depotIdList;
    }

    public Boolean getClientIsNull() {
        return clientIsNull;
    }

    public void setClientIsNull(Boolean clientIsNull) {
        this.clientIsNull = clientIsNull;
    }

    public Boolean getAdShop() {
        return adShop;
    }

    public void setAdShop(Boolean adShop) {
        this.adShop = adShop;
    }
}
