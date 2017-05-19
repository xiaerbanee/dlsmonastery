package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.DepotShopMapper;
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
    private DepotShopMapper depotShopMapper;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DepotShopDto> findPage(Pageable pageable, DepotQuery depotQuery){
        Page<DepotShopDto> page=depotShopMapper.findPage(pageable,depotQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DepotShopForm findForm(DepotShopForm depotShopForm) {
        if(!depotShopForm.isCreate()) {
            DepotShop depotShop =depotShopMapper.findOne(depotShopForm.getId());
            depotShopForm= BeanUtil.map(depotShop,DepotShopForm.class);
            cacheUtils.initCacheInput(depotShopForm);
        }
        return depotShopForm;
    }

    public DepotForm findDepotForm(DepotForm depotForm) {
        if(!depotForm.isCreate()) {
            Depot depot =depotMapper.findOne(depotForm.getId());
            depotForm= BeanUtil.map(depot,DepotForm.class);
            cacheUtils.initCacheInput(depotForm);
        }
        return depotForm;
    }

    public DepotShop save(DepotShopForm depotShopForm) {
        DepotShop depotShop;
        if(depotShopForm.isCreate()) {
            depotShop = BeanUtil.map(depotShopForm,DepotShop.class);
            depotShopMapper.save(depotShop);
            Depot depot=new Depot();
            depot.setName(depotShopForm.getDepotName());
            depot.setNamePinyin(StringUtils.getFirstSpell(depotShopForm.getDepotName()));
            depot.setDepotShopId(depotShop.getId());
            depotMapper.save(depot);
            depotShop.setDepotId(depot.getId());
            depotShopMapper.update(depotShop);
        } else {
            depotShop = depotShopMapper.findOne(depotShopForm.getId());
            ReflectionUtil.copyProperties(depotShopForm,depotShop);
            depotShopMapper.update(depotShop);
        }
        return depotShop;
    }

    public Depot saveDepot(DepotForm depotForm){
        Depot depot;
        depotForm.setNamePinyin(StringUtils.getFirstSpell(depotForm.getName()));
        if(depotForm.isCreate()){
            depot=BeanUtil.map(depotForm,Depot.class);
            depotMapper.save(depot);
            DepotShop depotShop=new DepotShop();
            depotShop.setDepotId(depot.getId());
            depotShopMapper.save(depotShop);
            depot.setDepotShopId(depotShop.getId());
            depotMapper.update(depot);
        }else {
            depot=depotMapper.findOne(depotForm.getId());
            ReflectionUtil.copyProperties(depotForm,depot);
            depotMapper.update(depot);
        }
        return depot;
    }


    public void logicDeleteOne(String id) {
        depotShopMapper.logicDeleteOne(id);
    }

}
