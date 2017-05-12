package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.HashBiMap;
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
public class DepotShopQuery extends BaseQuery {
    private String key;
    private List<String> officeIdList;
    private List<String> depotIdList;
    //直营
    private Boolean direct;
    //代理
    private Boolean delegate;
    //直营+子店
    private Boolean directAndSub;
    //广告门店
    private Boolean adShop;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Boolean getDirect() {
        return direct;
    }

    public void setDirect(Boolean direct) {
        this.direct = direct;
    }

    public Boolean getDelegate() {
        return delegate;
    }

    public void setDelegate(Boolean delegate) {
        this.delegate = delegate;
    }

    public Boolean getDirectAndSub() {
        return directAndSub;
    }

    public void setDirectAndSub(Boolean directAndSub) {
        this.directAndSub = directAndSub;
    }

    public Boolean getAdShop() {
        return adShop;
    }

    public void setAdShop(Boolean adShop) {
        this.adShop = adShop;
    }
}
