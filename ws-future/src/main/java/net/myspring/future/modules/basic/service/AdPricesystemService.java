package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.AdPricesystemDto;
import net.myspring.future.modules.basic.mapper.AdPricesystemMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.web.Query.AdPricesystemQuery;
import net.myspring.future.modules.basic.web.form.AdPricesystemForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
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

    public AdPricesystemDto findOne(String id){
        AdPricesystem adPricesystem = adPricesystemMapper.findOne(id);
        AdPricesystemDto adPricesystemDto = BeanUtil.map(adPricesystem,AdPricesystemDto.class);
        initDomain(Lists.newArrayList(adPricesystemDto));
        return adPricesystemDto;
    }

    public Page<AdPricesystemDto> findPage(Pageable pageable, AdPricesystemQuery adPricesystemQuery) {
        Page<AdPricesystemDto> page = adPricesystemMapper.findPage(pageable, adPricesystemQuery);
        initDomain(page.getContent());
        return page;
    }

    public List<AdPricesystemDto> findFilter(AdPricesystemQuery adPricesystemQuery){
        List<AdPricesystem> adPricesystemList = adPricesystemMapper.findFilter(adPricesystemQuery);
        List<AdPricesystemDto> adPricesystemDtoList = BeanUtil.map(adPricesystemList,AdPricesystemDto.class);
        initDomain(adPricesystemDtoList);
        return adPricesystemDtoList;
    }

    public void save(AdPricesystemForm adPricesystemForm){
        AdPricesystem adPricesystem;
        if(adPricesystemForm.isCreate()){
            adPricesystem = BeanUtil.map(adPricesystemForm,AdPricesystem.class);
            adPricesystemMapper.save(adPricesystem);
        }else{
            adPricesystemMapper.updateForm(adPricesystemForm);
        }
        List<Depot> depotList = depotMapper.findByIds(adPricesystemForm.getPageIds());
        for(Depot depot : depotList){
            if(!adPricesystemForm.getNewDepotIds().contains(depot.getId())){
                depot.setAdPricesystemId(null);
                depotMapper.update(depot);
            }else {
                if(!adPricesystemForm.getId().equals(depot.getAdPricesystemId())){
                    depot.setAdPricesystemId(adPricesystemForm.getId());
                    depotMapper.update(depot);
                }
            }
        }
    }

    public void delete(AdPricesystemForm adPricesystemForm){
        adPricesystemForm.setEnabled(false);
        adPricesystemMapper.updateForm(adPricesystemForm);
    }

    private void initDomain(List<AdPricesystemDto> adPricesystemList){
//        DomainUtils.initAuditing(adPricesystemList);
    }
}
