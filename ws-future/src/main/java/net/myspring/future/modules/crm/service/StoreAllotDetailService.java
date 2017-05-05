package net.myspring.future.modules.crm.service;

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
}
