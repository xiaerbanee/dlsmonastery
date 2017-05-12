package net.myspring.future.modules.basic.service;

import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.DepotShopMapper;
import net.myspring.future.modules.basic.web.query.DepotShopQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
public class DepotShopService {
    @Autowired
    private DepotShopMapper depotShopMapper;

    public List<DepotDto> findAll(DepotShopQuery depotShopQuery) {
        return depotShopMapper.findAll(depotShopQuery);
    }

}
