package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.Chain;
import net.myspring.future.modules.crm.domain.Depot;
import net.myspring.future.modules.crm.mapper.ChainMapper;
import net.myspring.future.modules.crm.mapper.DepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ChainService {

    @Autowired
    private ChainMapper chainMapper;
    @Autowired
    private DepotMapper depotMapper;

    public Chain findOne(String id) {
        Chain chain = chainMapper.findOne(id);
        return chain;
    }

    public Page<Chain> findPage(Pageable pageable, Map<String, Object> map) {
        Page<Chain> page = chainMapper.findPage(pageable, map);
        return page;
    }

    public void delete(Chain chain) {
        chain.setEnabled(false);
        chainMapper.update(chain);
    }

    @Transactional
    public void save(Chain chain) {
        List<Depot> depotList = depotMapper.findByIds(chain.getPageIds());
        for (Depot depot : depotList) {
            if (!chain.getNewDepotIdList().contains(depot.getId())) {
                depot.setChainId(null);
                depotMapper.update(depot);
            } else {
                if (!chain.getId().equals(depot.getChainId())) {
                    depot.setChainId(chain.getId());
                    depotMapper.update(depot);
                }
            }
        }
    }
}
