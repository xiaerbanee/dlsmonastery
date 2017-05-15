package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.dto.NameValueDto;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.dto.BasicDistrictDto;
import net.myspring.future.modules.layout.domain.ShopAttribute;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/18.
 */
public class DepotShopForm extends DataForm<Depot>{
    private String depotId;

    private DepotForm depotForm;

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public DepotForm getDepotForm() {
        return depotForm;
    }

    public void setDepotForm(DepotForm depotForm) {
        this.depotForm = depotForm;
    }
}
