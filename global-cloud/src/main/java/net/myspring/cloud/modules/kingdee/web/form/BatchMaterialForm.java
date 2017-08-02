package net.myspring.cloud.modules.kingdee.web.form;

import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.common.form.BaseForm;

/**
 * Created by lihx on 2017/8/1.
 */
public class BatchMaterialForm extends BaseForm<BdMaterial> {
    private String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
