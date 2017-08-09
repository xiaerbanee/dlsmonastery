package net.myspring.tool.modules.oppo.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.CacheUtils;
import net.myspring.tool.modules.future.dto.ProductDto;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.dto.OppoPlantAgentProductSelDto;
import net.myspring.tool.modules.oppo.repository.OppoPlantAgentProductSelRepository;
import net.myspring.tool.modules.oppo.web.query.OppoPlantAgentProductSelQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@LocalDataSource
public class OppoPlantAgentProductSelService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OppoPlantAgentProductSelRepository oppoPlantAgentProductSelRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public List<OppoPlantAgentProductSelDto> findAll(OppoPlantAgentProductSelQuery oppoPlantAgentProductSelQuery,List<ProductDto> productDtoList){
        List<String> productIdList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(productDtoList)){
            for (ProductDto productDto : productDtoList){
                productIdList.add(productDto.getId());
            }
        }
        oppoPlantAgentProductSelQuery.setProductIdList(productIdList);
        oppoPlantAgentProductSelQuery.setItemNumberList(StringUtils.getSplitList(oppoPlantAgentProductSelQuery.getItemNumberStr(), CharConstant.ENTER));
        List<OppoPlantAgentProductSelDto> oppoPlantAgentProductSelDtoList = oppoPlantAgentProductSelRepository.findAll(oppoPlantAgentProductSelQuery);
        cacheUtils.initCacheInput(oppoPlantAgentProductSelDtoList);
        return oppoPlantAgentProductSelDtoList;
    }

    @Transactional
    public void save(List<ProductDto> productDtoList, String data){
        List<Map<String,String>> list = ObjectMapperUtils.readValue(data, ArrayList.class);
        Map<String,ProductDto> productMap=Maps.newHashMap();
        for(ProductDto productDto:productDtoList){
            productMap.put(productDto.getName(),productDto);
        }
        List<OppoPlantAgentProductSel> oppoPlantAgentProductSels=oppoPlantAgentProductSelRepository.findAll();
        Map<String,OppoPlantAgentProductSel> oppoPlantAgentProductSelMap=Maps.newHashMap();
        for(OppoPlantAgentProductSel oppoPlantAgentProductSel:oppoPlantAgentProductSels){
            oppoPlantAgentProductSelMap.put(oppoPlantAgentProductSel.getId(),oppoPlantAgentProductSel);
        }
        for(Map<String,String> map:list){
            OppoPlantAgentProductSel agentProductSel=oppoPlantAgentProductSelMap.get(map.get("id"));
            ProductDto defaultProduct=productMap.get(map.get("productName"));
            ProductDto lxProduct=productMap.get(map.get("lxProductName"));
            agentProductSel.setProductId(defaultProduct==null?null:defaultProduct.getId());
            agentProductSel.setLxProductId(lxProduct==null?null:lxProduct.getId());
            oppoPlantAgentProductSelRepository.save(agentProductSel);
        }
    }
}
