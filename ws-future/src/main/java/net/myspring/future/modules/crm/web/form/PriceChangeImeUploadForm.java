package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.PriceChangeIme;

import java.util.List;

/**
 * Created by zhangyf on 2017/5/23.
 */
public class PriceChangeImeUploadForm extends BaseForm<PriceChangeIme> {

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
