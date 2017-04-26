package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.dto.ChainDto;
import net.myspring.future.modules.basic.manager.ChainManager;
import net.myspring.future.modules.basic.mapper.ChainMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.web.query.ChainQuery;
import net.myspring.future.modules.basic.web.form.ChainForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChainService {

    @Autowired
    private ChainMapper chainMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ChainManager chainManager;

    public Chain findOne(String id) {
        Chain chain = chainMapper.findOne(id);
        return chain;
    }

    public Page<ChainDto> findPage(Pageable pageable, ChainQuery chainQuery) {
        Page<ChainDto> page = chainMapper.findPage(pageable, chainQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(Chain chain) {
        chain.setEnabled(false);
        chainMapper.update(chain);
    }

    public ChainForm findForm(ChainForm chainForm){
        if(!chainForm.isCreate()){
            Chain chain=chainManager.findOne(chainForm.getId());
            chainForm= BeanUtil.map(chain,ChainForm.class);
            cacheUtils.initCacheInput(chainForm);
        }
        return chainForm;
    }

    public Chain save(ChainForm chainForm) {
        Chain chain;
        if(chainForm.isCreate()){
            chain=BeanUtil.map(chainForm,Chain.class);
            chain=chainManager.save(chain);
        }else{
            chain=chainManager.updateForm(chainForm);
        }
        /*List<Depot> depotList = depotMapper.findByIds(chainForm.getPageIds());
        for(Depot depot:depotList){
            if(!chainForm.getDepotIdList().contains(depot.getId())){
                depot.setChainId(null);
                depotMapper.update(depot);
            }else {
                if(!chain.getId().equals(depot.getChainId())){
                    depot.setChainId(chain.getId());
                    depotMapper.update(depot);
                }
            }
        }*/
        if(CollectionUtil.isNotEmpty(chainForm.getAccountIdList())){
            if(!chainForm.isCreate()){
                chainMapper.deleteByChainId(chain.getId());
            }
            chainMapper.saveAccountAndChain(chain.getId(),chainForm.getAccountIdList());
        }
        return chain;
    }
}
