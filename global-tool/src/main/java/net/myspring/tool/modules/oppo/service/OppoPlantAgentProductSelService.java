package net.myspring.tool.modules.oppo.service;


import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.ProductClient;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.domain.ProductEntity;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.dto.OppoPlantAgentProductSelDto;
import net.myspring.tool.modules.oppo.repository.OppoPlantAgentProductSelRepository;
import net.myspring.tool.modules.oppo.web.form.OppoPlantAgentProductSelForm;
import net.myspring.tool.modules.oppo.web.query.OppoPlantAgentProductSelQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@LocalDataSource
public class OppoPlantAgentProductSelService {

    @Autowired
    private ProductClient productClient;
    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private OppoPlantAgentProductSelRepository oppoPlantAgentProductSelRepository;

    public List<OppoPlantAgentProductSelDto> findAll(OppoPlantAgentProductSelQuery oppoPlantAgentProductSelQuery){
        oppoPlantAgentProductSelQuery.setItemNumberList(StringUtils.getSplitList(oppoPlantAgentProductSelQuery.getItemNumberStr(), CharConstant.ENTER));
        List<OppoPlantAgentProductSelDto> oppoPlantAgentProductSelDtoList = oppoPlantAgentProductSelRepository.findAll(oppoPlantAgentProductSelQuery);
        return oppoPlantAgentProductSelDtoList;
    }

    public OppoPlantAgentProductSelForm getForm(OppoPlantAgentProductSelForm oppoPlantAgentProductSelForm){
        String lxAgentCodes = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.LX_FACTORY_AGENT_CODES.name());
        oppoPlantAgentProductSelForm.setLx(StringUtils.isNotBlank(lxAgentCodes));
        oppoPlantAgentProductSelForm.getExtra().put("productNames",CollectionUtil.extractToList(productClient.findHasImeProduct(),"name"));
        return oppoPlantAgentProductSelForm;
    }

    @Transactional
    public void save(String data){
        List<Map<String,String>> list = ObjectMapperUtils.readValue(data, ArrayList.class);
        List<ProductEntity> productEntityList=productClient.findHasImeProduct();
        Map<String,ProductEntity> productMap=Maps.newHashMap();
        for(ProductEntity productEntity:productEntityList){
            productMap.put(productEntity.getName(),productEntity);
        }
        List<OppoPlantAgentProductSel> oppoPlantAgentProductSels=oppoPlantAgentProductSelRepository.findAll();
        Map<String,OppoPlantAgentProductSel> oppoPlantAgentProductSelMap=Maps.newHashMap();
        for(OppoPlantAgentProductSel oppoPlantAgentProductSel:oppoPlantAgentProductSels){
            oppoPlantAgentProductSelMap.put(oppoPlantAgentProductSel.getId(),oppoPlantAgentProductSel);
        }
        for(Map<String,String> map:list){
            OppoPlantAgentProductSel agentProductSel=oppoPlantAgentProductSelMap.get(map.get("id"));
            ProductEntity defaultProduct=productMap.get(map.get("productName"));
            ProductEntity lxProduct=productMap.get(map.get("lxProductName"));
            agentProductSel.setProductId(defaultProduct==null?null:defaultProduct.getId());
            agentProductSel.setLxProductId(lxProduct==null?null:lxProduct.getId());
            oppoPlantAgentProductSelRepository.save(agentProductSel);
        }
    }
}
