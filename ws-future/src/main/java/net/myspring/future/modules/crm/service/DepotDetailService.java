package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.DepotDetail;
import net.myspring.future.modules.crm.mapper.DepotDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DepotDetailService {

    @Autowired
    private DepotDetailMapper depotDetailMapper;

    public DepotDetail findOne(String id) {
        DepotDetail depotDetail=depotDetailMapper.findOne(id);
        return depotDetail;
    }

    public Page<DepotDetail> findPage(Pageable pageable, Map<String, Object> map) {
        Page<DepotDetail> page = depotDetailMapper.findPage(pageable, map);
        return page;
    }
}
