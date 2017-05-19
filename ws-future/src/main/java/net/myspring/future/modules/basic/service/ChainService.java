package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.dto.ChainDto;
import net.myspring.future.modules.basic.dto.PricesystemDto;
import net.myspring.future.modules.basic.mapper.ChainMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.DepotShopMapper;
import net.myspring.future.modules.basic.web.query.ChainQuery;
import net.myspring.future.modules.basic.web.form.ChainForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ChainService {

    @Autowired
    private ChainMapper chainMapper;
    @Autowired
    private DepotShopMapper depotShopMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public List<ChainDto> findAllEnabled(){
        List<Chain> chainList=chainMapper.findAllEnabled();
        List<ChainDto> chainDtoList= BeanUtil.map(chainList,ChainDto.class);
        return chainDtoList;
    }

    public Chain findOne(String id) {
        Chain chain = chainMapper.findOne(id);
        return chain;
    }

    public Page<ChainDto> findPage(Pageable pageable, ChainQuery chainQuery) {
        Page<ChainDto> page = chainMapper.findPage(pageable, chainQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(ChainForm chainForm) {
        chainMapper.logicDeleteOne(chainForm.getId());
    }

    public ChainForm getFormProperty(ChainForm chainForm){
        if(!chainForm.isCreate()){
            Chain chain=chainMapper.findOne(chainForm.getId());
            chainForm= BeanUtil.map(chain,ChainForm.class);
            cacheUtils.initCacheInput(chainForm);
        }
        return chainForm;
    }

    public Chain save(ChainForm chainForm) {
        Chain chain;
        if(chainForm.isCreate()){
            chain=BeanUtil.map(chainForm,Chain.class);
            chainMapper.save(chain);
        }else{
            chain = chainMapper.findOne(chainForm.getId());
            ReflectionUtil.copyProperties(chainForm,chain);
            chainMapper.update(chain);
            chainMapper.deleteByChainId(chain.getId());
        }
        if(CollectionUtil.isNotEmpty(chainForm.getAccountIdList())){
            chainMapper.saveAccountAndChain(chain.getId(),chainForm.getAccountIdList());
        }
        return chain;
    }
}
