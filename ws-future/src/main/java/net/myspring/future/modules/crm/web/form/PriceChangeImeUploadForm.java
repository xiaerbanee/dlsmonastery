package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.PriceChangeIme;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ProductImeDto;

import java.util.List;

/**
 * Created by zhangyf on 2017/5/23.
 */
public class PriceChangeImeUploadForm extends DataForm<PriceChangeIme>{

    private String priceChangeId;

    private List<List<String>> imeUploadList;

    public String getPriceChangeId() {
        return priceChangeId;
    }

    public void setPriceChangeId(String priceChangeId) {
        this.priceChangeId = priceChangeId;
    }

    public List<List<String>> getImeUploadList() {
        return imeUploadList;
    }

    public void setImeUploadList(List<List<String>> imeUploadList) {
        this.imeUploadList = imeUploadList;
    }
}
