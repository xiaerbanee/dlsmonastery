package net.myspring.future.modules.crm.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.util.text.StringUtils;

import java.util.List;

public class StoreAllotShipForm extends BaseForm<StoreAllot> {

    private String boxImeStr;
    private String imeStr;
    private String expressOrderExpressCodes;
    private String shipRemarks;

    public String getBoxImeStr() {
        return boxImeStr;
    }

    public void setBoxImeStr(String boxImeStr) {
        this.boxImeStr = boxImeStr;
    }

    public String getImeStr() {
        return imeStr;
    }

    public void setImeStr(String imeStr) {
        this.imeStr = imeStr;
    }

    public String getExpressOrderExpressCodes() {
        return expressOrderExpressCodes;
    }

    public void setExpressOrderExpressCodes(String expressOrderExpressCodes) {
        this.expressOrderExpressCodes = expressOrderExpressCodes;
    }

    public String getShipRemarks() {
        return shipRemarks;
    }

    public void setShipRemarks(String shipRemarks) {
        this.shipRemarks = shipRemarks;
    }

    public List<String> getImeList() {
        List<String> list =  StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        List<String> imeList = Lists.newArrayList();
        for(String ime:list) {
            if (ime.startsWith("86")) {
                imeList.add(StringUtils.getNumberStr(ime));
            } else {
                imeList.add(ime);
            }
        }
        return imeList;
    }

    public List<String> getBoxImeList() {
        return StringUtils.getSplitList(boxImeStr, CharConstant.ENTER);
    }

    public List<String> getExpressOrderExpressCodeList() {
        return StringUtils.getSplitList(expressOrderExpressCodes, CharConstant.ENTER);
    }

}
