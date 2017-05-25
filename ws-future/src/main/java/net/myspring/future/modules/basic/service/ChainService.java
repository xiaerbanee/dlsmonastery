package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.ChainDto;
import net.myspring.future.modules.basic.mapper.ChainMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.repository.ChainRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.web.form.ChainForm;
import net.myspring.future.modules.basic.web.query.ChainQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChainService {

    @Autowired
    private ChainMapper chainMapper;
    @Autowired
    private ChainRepository chainRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private DepotRepository depotRepository;

    public List<ChainDto> findAllEnabled() {
        List<Chain> chainList = chainRepository.findAllEnabled();
        List<ChainDto> chainDtoList = BeanUtil.map(chainList, ChainDto.class);
        return chainDtoList;
    }

    public ChainDto findOne(String id) {
        ChainDto chainDto = null;
        if (StringUtils.isBlank(id)) {
            return new ChainDto();
        } else {
            Chain chain = chainRepository.findOne(id);
            chainDto = BeanUtil.map(chain, ChainDto.class);
            chainDto.setDepotIdList(chainRepository.findDepotIds(chainDto.getId()));
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
        chainRepository.logicDeleteOne(id);
    }

    public Chain save(ChainForm chainForm) {
        Chain chain;
        if (chainForm.isCreate()) {
            chain = BeanUtil.map(chainForm, Chain.class);
            chainRepository.save(chain);
        } else {
            chain = chainRepository.findOne(chainForm.getId());
            ReflectionUtil.copyProperties(chainForm, chain);
            chainRepository.save(chain);
            List<Depot> depotList = depotRepository.findByChainId(chainForm.getId());
            for (Depot depot : depotList) {
                depot.setChainId(null);
                depotRepository.save(depot);
            }
        }
        //保存门店
        if (CollectionUtil.isNotEmpty(chainForm.getDepotIdList())) {
            List<Depot> depotList = depotRepository.findByIds(chainForm.getDepotIdList());
            for (Depot depot : depotList) {
                depot.setChainId(chain.getId());
                depotRepository.save(depot);
            }
        }
        return chain;
    }
}
