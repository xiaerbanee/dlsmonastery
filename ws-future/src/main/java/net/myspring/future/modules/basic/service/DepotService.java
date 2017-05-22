package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DepotService {
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private OfficeClient officeClient;


    public List<DepotDto> findShopList(DepotQuery depotQuery) {
        List<Depot> depotList = depotMapper.findByAccountId(RequestUtils.getAccountId());
        depotQuery.setOfficeIdList(officeClient.getOfficeFilterIds(RequestUtils.getRequestEntity().getOfficeId()));
        if(CollectionUtil.isNotEmpty(depotList)) {
            depotQuery.setDepotIdList(CollectionUtil.extractToList(depotList,"id"));
        }
        return depotMapper.findShopList(depotQuery);
    }

    public List<DepotDto> findStoreList(DepotQuery depotQuery) {
        List<Depot> depotList = depotMapper.findByAccountId(RequestUtils.getAccountId());
        depotQuery.setOfficeIdList(officeClient.getOfficeFilterIds(RequestUtils.getRequestEntity().getOfficeId()));
        if(CollectionUtil.isNotEmpty(depotList)) {
            depotQuery.setDepotIdList(CollectionUtil.extractToList(depotList,"id"));
        }
        return depotMapper.findStoreList(depotQuery);
    }



    public List<DepotDto> findStoreList(String shipType) {
        //TODO  需要完成根據shipType選擇倉庫
        return findStoreList(new DepotQuery());

    }



    public List<DepotDto> findByIds(List<String> ids){
        List<Depot> depotList=depotMapper.findByIds(ids);
        List<DepotDto> depotDtoList= BeanUtil.map(depotList,DepotDto.class);
        return depotDtoList;
    }

    public DepotDto findById(String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        List<DepotDto> depotList=findByIds(ids);
        if(depotList!=null && depotList.size() >=1){
            return depotList.get(0);
        }else{
            return null;
        }
    }
}
