package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import net.myspring.future.modules.crm.mapper.StoreAllotDetailMapper;
import net.myspring.future.modules.crm.web.form.StoreAllotDetailForm;
import net.myspring.future.modules.crm.web.form.StoreAllotForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StoreAllotDetailService {

    @Autowired
    private StoreAllotDetailMapper storeAllotDetailMapper;
    @Autowired
    private CacheUtils cacheUtils;

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
//        storeAllotDetailMapper.deleteByStoreAllotId(storeAllotId);
        if(storeAllotDetailFormList == null){
            return;
        }

        List<StoreAllotDetail> toBeSaved = Lists.newArrayList();
        for(StoreAllotDetailForm each : storeAllotDetailFormList){
            if(each == null || each.getBillQty() == null || each.getBillQty() <=0){
                continue;
            }
            StoreAllotDetail storeAllotDetail = BeanUtil.map(each, StoreAllotDetail.class);
            storeAllotDetail.setStoreAllotId(storeAllotId);
            toBeSaved.add(storeAllotDetail);
        }
        if(!toBeSaved.isEmpty()){
            storeAllotDetailMapper.batchSave(toBeSaved);
        }

    }

    public List<StoreAllotDetailForm> getStoreAllotDetailListForNew() {
        List<StoreAllotDetailDto> result = storeAllotDetailMapper.getStoreAllotDetailListForNew(RequestUtils.getCompanyId());
        cacheUtils.initCacheInput(result);

        return BeanUtil.map(result, StoreAllotDetailForm.class);
    }

    public List<StoreAllotDetailForm> findStoreAllotDetailsForFastAllot(LocalDate billDate, String toStockId, String status) {

        List<StoreAllotDetailDto> tmp = storeAllotDetailMapper.findStoreAllotDetailsForFastAllot(billDate, toStockId, status);

        cacheUtils.initCacheInput(tmp);
        return BeanUtil.map(tmp, StoreAllotDetailForm.class);
    }
}
