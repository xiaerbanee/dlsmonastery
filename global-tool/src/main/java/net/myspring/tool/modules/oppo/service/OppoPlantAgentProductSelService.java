package net.myspring.tool.modules.oppo.service;


import net.myspring.common.constant.CharConstant;
import net.myspring.tool.common.client.ProductClient;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.client.CustomerClient;
import net.myspring.tool.common.utils.CacheUtils;
import net.myspring.tool.modules.oppo.dto.OppoPlantAgentProductSelDto;
import net.myspring.tool.modules.oppo.repository.OppoPlantAgentProductSelRepository;
import net.myspring.tool.modules.oppo.web.query.OppoPlantAgentProductSelQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@LocalDataSource
@Transactional
public class OppoPlantAgentProductSelService {

    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OppoPlantAgentProductSelRepository oppoPlantAgentProductSelRepository;

    public List<OppoPlantAgentProductSelDto> findFilter(OppoPlantAgentProductSelQuery oppoPlantAgentProductSelQuery){
        oppoPlantAgentProductSelQuery.setItemNumberList(StringUtils.getSplitList(oppoPlantAgentProductSelQuery.getItemNumberStr(), CharConstant.ENTER));
        List<OppoPlantAgentProductSelDto> oppoPlantAgentProductSelDtoList = oppoPlantAgentProductSelRepository.findFilter(oppoPlantAgentProductSelQuery);
        return oppoPlantAgentProductSelDtoList;
    }

    public List<String> findHasImeProduct(){
        System.out.println("--------"+CollectionUtil.extractToList(productClient.findHasImeProduct(),"name").size());
        return CollectionUtil.extractToList(productClient.findHasImeProduct(),"name");
    }
}
