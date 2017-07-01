package net.myspring.tool.modules.oppo.service;


import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.client.ProductClient;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.client.CustomerClient;
import net.myspring.tool.modules.oppo.dto.OppoPlantAgentProductSelDto;
import net.myspring.tool.modules.oppo.repository.OppoPlantAgentProductSelRepository;
import net.myspring.tool.modules.oppo.web.form.OppoPlantAgentProductSqlForm;
import net.myspring.tool.modules.oppo.web.query.OppoPlantAgentProductSelQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.apache.commons.math.stat.descriptive.summary.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@LocalDataSource
@Transactional
public class OppoPlantAgentProductSelService {

    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private OppoPlantAgentProductSelRepository oppoPlantAgentProductSelRepository;

    public List<OppoPlantAgentProductSelDto> findFilter(OppoPlantAgentProductSelQuery oppoPlantAgentProductSelQuery){
        oppoPlantAgentProductSelQuery.setItemNumberList(StringUtils.getSplitList(oppoPlantAgentProductSelQuery.getItemNumberStr(), CharConstant.ENTER));
        List<OppoPlantAgentProductSelDto> oppoPlantAgentProductSelDtoList = oppoPlantAgentProductSelRepository.findFilter(oppoPlantAgentProductSelQuery);
        return oppoPlantAgentProductSelDtoList;
    }

    public OppoPlantAgentProductSqlForm form(OppoPlantAgentProductSqlForm oppoPlantAgentProductSqlForm){
        String isLx = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name());
        if(StringUtils.isNotBlank(isLx)){
            oppoPlantAgentProductSqlForm.setLx(true);
        }else{
            oppoPlantAgentProductSqlForm.setLx(false);
        }
        oppoPlantAgentProductSqlForm.getExtra().put("productNames",CollectionUtil.extractToList(productClient.findHasImeProduct(),"name"));
        return oppoPlantAgentProductSqlForm;
    }

    public void save(String data){
        String isLx = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name());
        List<OppoPlantAgentProductSelDto> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(StringUtils.isNotBlank(isLx)){

        }else{

        }

    }

}
