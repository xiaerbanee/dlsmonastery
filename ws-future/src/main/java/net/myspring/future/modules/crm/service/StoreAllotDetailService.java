package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import net.myspring.future.modules.crm.mapper.StoreAllotDetailMapper;
import net.myspring.future.modules.crm.web.form.StoreAllotDetailForm;
import net.myspring.future.modules.crm.web.form.StoreAllotForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreAllotDetailService {

    @Autowired
    private StoreAllotDetailMapper storeAllotDetailMapper;


    public List<StoreAllotDetailDto> findByStoreAllotIds(List<String> storeAllotIds) {
        return storeAllotDetailMapper.findByStoreAllotIds(storeAllotIds);
    }

    public List<StoreAllotDetailDto> findByStoreAllotId(String storeAllotId) {
        List<String> storeAllotIds = new ArrayList<>();
        storeAllotIds.add(storeAllotId);
        return storeAllotDetailMapper.findByStoreAllotIds(storeAllotIds);
    }

    public List<StoreAllotDetailForm> genStoreAllotDetailListForEdit(StoreAllotForm storeAllotForm) {
        List<StoreAllotDetailForm> result = new ArrayList<>();
        if(storeAllotForm.isCreate()){
            return result;
        }
        List<String> storeAllotIds = new ArrayList<>();
        storeAllotIds.add(storeAllotForm.getId());
        List<StoreAllotDetailDto> tmp = findByStoreAllotIds(storeAllotIds);
        if(tmp!=null){
            result = BeanUtil.map(tmp, StoreAllotDetailForm.class);

        }
        return result;
    }

    public void saveStoreAllotDetails(String storeAllotId, List<StoreAllotDetailForm> storeAllotDetailFormList) {

        List<StoreAllotDetail> toBeSaved = Lists.newArrayList();
        for(int i=storeAllotDetailFormList.size()-1 ; i >= 0; i--){
            StoreAllotDetailForm storeAllotDetailForm = storeAllotDetailFormList.get(i);
            if(storeAllotDetailForm.getBillQty() != null || storeAllotDetailForm.getBillQty() > 0) {
                StoreAllotDetail storeAllotDetail = BeanUtil.map(storeAllotDetailForm, StoreAllotDetail.class);
                storeAllotDetail.setStoreAllotId(storeAllotId);
                toBeSaved.add(storeAllotDetail);
            }
        }
        storeAllotDetailMapper.batchSave(toBeSaved);
    }
}
