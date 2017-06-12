package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ImeAllot;

import java.util.List;


public class ImeAllotBatchForm extends BaseForm<ImeAllot> {

    private List<ImeAllotSimpleForm> imeAllotSimpleFormList;

    public List<ImeAllotSimpleForm> getImeAllotSimpleFormList() {
        return imeAllotSimpleFormList;
    }

    public void setImeAllotSimpleFormList(List<ImeAllotSimpleForm> imeAllotSimpleFormList) {
        this.imeAllotSimpleFormList = imeAllotSimpleFormList;
    }
}
