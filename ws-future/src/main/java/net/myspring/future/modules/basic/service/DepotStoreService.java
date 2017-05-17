package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.dto.DepotStoreDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.mapper.DepotStoreMapper;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.form.DepotStoreForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.basic.web.query.DepotStoreQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
public class DepotStoreService {
    @Autowired
    private DepotStoreMapper depotStoreMapper;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DepotStoreDto> findPage(Pageable pageable, DepotStoreQuery depotStoreQuery){
        Page<DepotStoreDto> page=depotStoreMapper.findPage(pageable,depotStoreQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DepotStoreForm findForm(DepotStoreForm depotStoreForm) {
        if(!depotStoreForm.isCreate()) {
            DepotStore depotStore =depotStoreMapper.findOne(depotStoreForm.getId());
            depotStoreForm= BeanUtil.map(depotStore,DepotStoreForm.class);
            cacheUtils.initCacheInput(depotStoreForm);
        }
        return depotStoreForm;
    }

    public DepotStore save(DepotStoreForm depotStoreForm) {
        DepotStore depotStore;
        DepotForm depotForm = depotStoreForm.getDepotForm();
        //保存depot
        Depot depot = BeanUtil.map(depotForm, Depot.class);
        depotManager.save(depot);
        //保存depotStore
        if(depotStoreForm.isCreate()) {
            depotStoreForm.setDepotId(depot.getId());
            depotStore = BeanUtil.map(depotStoreForm,DepotStore.class);
            depotStoreMapper.save(depotStore);
        } else {
            depotStore = depotStoreMapper.findOne(depotStoreForm.getId());
        }
        return depotStore;
    }


    public void logicDeleteOne(String id) {
        depotStoreMapper.logicDeleteOne(id);
    }


}