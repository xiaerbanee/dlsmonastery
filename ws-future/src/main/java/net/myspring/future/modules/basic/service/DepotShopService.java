package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.mapper.DepotShopMapper;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.form.DepotShopForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
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
    private CacheUtils cacheUtils;

    public Page<DepotShopDto> findPage(Pageable pageable, DepotQuery depotShopQuery){
        Page<DepotShopDto> page=depotShopMapper.findPage(pageable,depotShopQuery);
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

    public DepotShop save(DepotShopForm depotShopForm) {
        DepotShop depotShop;
        DepotForm depotForm = depotShopForm.getDepotForm();
        //保存depot
        Depot depot = BeanUtil.map(depotForm, Depot.class);
        depotManager.save(depot);
        //保存depotShop
        if(depotShopForm.isCreate()) {
            depotShopForm.setDepotId(depot.getId());
            depotShop = BeanUtil.map(depotShopForm,DepotShop.class);
            depotShopMapper.save(depotShop);
        } else {
            depotShop = depotShopMapper.findOne(depotShopForm.getId());
        }
        return depotShop;
    }


    public void logicDeleteOne(String id) {
        depotShopMapper.logicDeleteOne(id);
    }

}
