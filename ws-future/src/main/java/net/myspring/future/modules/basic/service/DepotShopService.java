package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotShopRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotShopRepository;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.form.DepotShopForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
public class DepotShopService {
    @Autowired
    private DepotShopRepository depotShopRepository;
    @Autowired
    private DepotShopRepository depotShopRepository;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DepotShopDto> findPage(Pageable pageable, DepotQuery depotQuery){
        Page<DepotShopDto> page=depotShopRepository.findPage(pageable,depotQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DepotShopForm getForm(DepotShopForm depotShopForm) {
        if(!depotShopForm.isCreate()) {
            DepotShop depotShop =depotShopRepository.findOne(depotShopForm.getId());
            depotShopForm= BeanUtil.map(depotShop,DepotShopForm.class);
            cacheUtils.initCacheInput(depotShopForm);
        }
        return depotShopForm;
    }

    public DepotForm findDepotForm(DepotForm depotForm) {
        if(!depotForm.isCreate()) {
            Depot depot =depotRepository.findOne(depotForm.getId());
            depotForm= BeanUtil.map(depot,DepotForm.class);
            cacheUtils.initCacheInput(depotForm);
        }
        return depotForm;
    }

    public DepotShop save(DepotShopForm depotShopForm) {
        DepotShop depotShop;
        if(depotShopForm.isCreate()) {
            depotShop = BeanUtil.map(depotShopForm,DepotShop.class);
            depotShopRepository.save(depotShop);
            Depot depot=new Depot();
            depot.setName(depotShopForm.getDepotName());
            depot.setNamePinyin(StringUtils.getFirstSpell(depotShopForm.getDepotName()));
            depot.setDepotShopId(depotShop.getId());
            depotRepository.save(depot);
            depotShop.setDepotId(depot.getId());
            depotShopRepository.save(depotShop);
        } else {
            depotShop = depotShopRepository.findOne(depotShopForm.getId());
            ReflectionUtil.copyProperties(depotShopForm,depotShop);
            depotShopRepository.save(depotShop);
        }
        return depotShop;
    }

    public Depot saveDepot(DepotForm depotForm){
        Depot depot;
        depotForm.setNamePinyin(StringUtils.getFirstSpell(depotForm.getName()));
        if(depotForm.isCreate()){
            depot=BeanUtil.map(depotForm,Depot.class);
            depotRepository.save(depot);
            DepotShop depotShop=new DepotShop();
            depotShop.setDepotId(depot.getId());
            depotShopRepository.save(depotShop);
            depot.setDepotShopId(depotShop.getId());
            depotRepository.save(depot);
        }else {
            depot=depotRepository.findOne(depotForm.getId());
            ReflectionUtil.copyProperties(depotForm,depot);
            depotRepository.save(depot);
        }
        return depot;
    }


    public void logicDeleteOne(String id) {
        depotShopRepository.logicDeleteOne(id);
    }

}
