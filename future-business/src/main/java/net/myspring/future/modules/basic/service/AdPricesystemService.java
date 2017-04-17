package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.dto.AdPricesystemDto;
import net.myspring.future.modules.basic.mapper.AdPricesystemMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AdPricesystemService {

    @Autowired
    private AdPricesystemMapper adPricesystemMapper;
    @Autowired
    private DepotMapper depotMapper;

//    public AdPricesystemDto findOne(String id){
//        AdPricesystem adPricesystem = adPricesystemMapper.findOne(id);
//        AdPricesystemDto adPricesystemDto = BeanUtil.map(adPricesystem,AdPricesystemDto.class)
//        initDomain(Lists.newArrayList(adPricesystemDto));
//        return adPricesystemDto;
//    }
//
//    public Page<AdPricesystem> findPage(Pageable pageable, Map<String, Object> map) {
//        Page<AdPricesystem> page = adPricesystemMapper.findPage(pageable, map);
//        initDomain(page.getContent());
//        return page;
//    }
//
//    public List<AdPricesystem> findFilter( Map<String, Object> map){
//        List<AdPricesystem> adPricesystemList = adPricesystemMapper.findFilter(map);
//        initDomain(adPricesystemList);
//        return adPricesystemList;
//    }
//
//    @Transactional
//    public void save(AdPricesystem adPricesystem){
//        Boolean isCreated =adPricesystem.isCreate();
//        if(isCreated){
//            adPricesystemMapper.save(adPricesystem);
//        }else{
//            adPricesystemMapper.update(adPricesystem);
//        }
//        List<Depot> depotList = depotMapper.findByIds(adPricesystem.getPageIds());
//        for(Depot depot:depotList){
//            if(!adPricesystem.getNewDepotIds().contains(depot.getId())){
//                depot.setAdPricesystemId(null);
//                depotMapper.update(depot);
//            }else {
//                if(!adPricesystem.getId().equals(depot.getAdPricesystemId())){
//                    depot.setAdPricesystemId(adPricesystem.getId());
//                    depotMapper.update(depot);
//                }
//            }
//        }
//    }
//
//    public void delete(AdPricesystem adPricesystem){
//        adPricesystem.setEnabled(false);
//        adPricesystemMapper.update(adPricesystem);
//    }
//
//
//    private void initDomain(List<AdPricesystemDto> adPricesystemList){
//        DomainUtils.initAuditing(adPricesystemList);
//    }
}
