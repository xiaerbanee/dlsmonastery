package net.myspring.future.modules.crm.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.crm.domain.PricesystemChange;

import java.util.List;

/**
 * Created by haos on 2017/5/17.
 */
public class PricesystemChangeForm extends DataForm<PricesystemChange>{
    private String productId;
    private String remarks;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
