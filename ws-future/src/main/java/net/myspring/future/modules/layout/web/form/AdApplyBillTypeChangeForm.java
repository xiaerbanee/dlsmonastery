package net.myspring.future.modules.layout.web.form;

import net.myspring.future.modules.layout.dto.AdApplyDto;

import java.util.List;

/**
 * Created by zhangyf on 2017/6/23.
 */
public class AdApplyBillTypeChangeForm {
    private String storeId;
    private List<AdApplyDto> adApplyDtoList;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<AdApplyDto> getAdApplyDtoList() {
        return adApplyDtoList;
    }

    public void setAdApplyDtoList(List<AdApplyDto> adApplyDtoList) {
        this.adApplyDtoList = adApplyDtoList;
    }
}
