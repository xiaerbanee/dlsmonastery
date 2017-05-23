package net.myspring.future.modules.basic.service;

import com.ctc.wstx.util.StringUtil;
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
import org.apache.commons.lang.StringUtils;
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
    private CacheUtils cacheUtils;
    @Autowired
    private DepotMapper depotMapper;

    public List<ChainDto> findAllEnabled() {
        List<Chain> chainList = chainMapper.findAllEnabled();
        List<ChainDto> chainDtoList = BeanUtil.map(chainList, ChainDto.class);
        return chainDtoList;
    }

    public ChainDto findOne(String id) {
        ChainDto chainDto;
        if (StringUtils.isBlank(id)) {
            chainDto = new ChainDto();
        } else {
            Chain chain = chainMapper.findOne(id);
            chainDto = BeanUtil.map(chain, ChainDto.class);
            chainDto.setDepotIdList(chainMapper.findDepotIds(chainDto.getId()));
            cacheUtils.initCacheInput(chainDto);
        }
        return chainDto;
    }

    public Page<ChainDto> findPage(Pageable pageable, ChainQuery chainQuery) {
        Page<ChainDto> page = chainMapper.findPage(pageable, chainQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void logicDeleteOne(String id) {
        chainMapper.logicDeleteOne(id);
    }

    public Chain save(ChainForm chainForm) {
        Chain chain;
        if (chainForm.isCreate()) {
            chain = BeanUtil.map(chainForm, Chain.class);
            chainMapper.save(chain);
        } else {
            chain = chainMapper.findOne(chainForm.getId());
            ReflectionUtil.copyProperties(chainForm, chain);
            chainMapper.update(chain);
            List<Depot> depotList = depotMapper.findByChainId(chainForm.getId());
            for (Depot depot : depotList) {
                depot.setChainId(null);
                depotMapper.update(depot);
            }
        }
        //保存门店
        if (CollectionUtil.isNotEmpty(chainForm.getDepotIdList())) {
            List<Depot> depotList = depotMapper.findByIds(chainForm.getDepotIdList());
            for (Depot depot : depotList) {
                depot.setChainId(chain.getId());
                depotMapper.update(depot);
            }
        }
        return chain;
    }
}
