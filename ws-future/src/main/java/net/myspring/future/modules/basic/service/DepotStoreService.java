package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.dto.DepotStoreDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.DepotStoreMapper;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.form.DepotStoreForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.basic.web.query.DepotStoreQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.mapper.BeanMapper;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
@Transactional
public class DepotStoreService {
    @Autowired
    private DepotStoreMapper depotStoreMapper;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotMapper depotMapper;

    public Page<DepotStoreDto> findPage(Pageable pageable, DepotStoreQuery depotStoreQuery){
        Page<DepotStoreDto> page=depotStoreMapper.findPage(pageable,depotStoreQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DepotStoreForm getForm(DepotStoreForm depotStoreForm) {
        if(!depotStoreForm.isCreate()) {
            DepotStore depotStore =depotStoreMapper.findOne(depotStoreForm.getId());
            depotStoreForm= BeanUtil.map(depotStore,DepotStoreForm.class);
            Depot depot=depotMapper.findOne(depotStoreForm.getDepotId());
            depotStoreForm.setDepotForm(BeanUtil.map(depot,DepotForm.class));
            cacheUtils.initCacheInput(depotStoreForm);
        }
        return depotStoreForm;
    }

    public DepotStore save(DepotStoreForm depotStoreForm) {
        DepotStore depotStore;
        DepotForm depotForm = depotStoreForm.getDepotForm();
        //保存depot
        Depot depot = BeanUtil.map(depotForm, Depot.class);
        depot.setNamePinyin(StringUtils.getFirstSpell(depotStoreForm.getDepotForm().getName()));
        depotManager.save(depot);
        //保存depotStore
        if(depotStoreForm.isCreate()) {
            depotStoreForm.setDepotId(depot.getId());
            depotStore = BeanUtil.map(depotStoreForm,DepotStore.class);
            depotStoreMapper.save(depotStore);
        } else {
            depotStore = depotStoreMapper.findOne(depotStoreForm.getId());
            ReflectionUtil.copyProperties(depotStoreForm,depotStore);
            depotStore.setDepotId(depot.getId());
            depotStoreMapper.update(depotStore);
        }
        return depotStore;
    }


    public void logicDeleteOne(String id) {
        depotStoreMapper.logicDeleteOne(id);
    }


}
