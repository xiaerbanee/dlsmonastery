package net.myspring.tool.modules.oppo.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.dto.OppoPlantAgentProductSelDto;

import java.util.List;

public class OppoPlantAgentProductSqlForm extends BaseForm<OppoPlantAgentProductSel>{
    private Boolean lx;

    public Boolean getLx() {
        return lx;
    }

    public void setLx(Boolean lx) {
        this.lx = lx;
    }
}
