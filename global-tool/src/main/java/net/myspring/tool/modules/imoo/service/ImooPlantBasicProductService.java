package net.myspring.tool.modules.imoo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.CacheUtils;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.future.dto.ProductDto;
import net.myspring.tool.modules.imoo.dto.ImooPlantBasicProductDto;
import net.myspring.tool.modules.imoo.dto.ImooProductDto;
import net.myspring.tool.modules.imoo.repository.ImooPlantBasicProductRepository;
import net.myspring.tool.modules.imoo.repository.ImooProductMapRepository;
import net.myspring.tool.modules.imoo.web.query.ImooPlantBasicProductQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@LocalDataSource
public class ImooPlantBasicProductService {
    @Autowired
    private ImooPlantBasicProductRepository imooPlantBasicProductRepository;
    @Autowired
    private ImooProductMapRepository imooProductMapRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public List<ImooPlantBasicProductDto> findAll(ImooPlantBasicProductQuery imooPlantBasicProductQuery){
        List<String> list = StringUtils.getSplitList(imooPlantBasicProductQuery.getSegment1(), CharConstant.ENTER);
        imooPlantBasicProductQuery.setSegment1List(list);
        List<ImooPlantBasicProductDto> imooPlantBasicProductList = imooPlantBasicProductRepository.findAll(imooPlantBasicProductQuery);
        cacheUtils.initCacheInput(imooPlantBasicProductList);
        return imooPlantBasicProductList;
    }

    @Transactional
    public void save(List<ProductDto> productDtoList,String data){
        List<Map<String,String>> list = ObjectMapperUtils.readValue(data, ArrayList.class);
        Map<String,ProductDto> productMap= Maps.newHashMap();
        for (ProductDto productDto:productDtoList){
            productMap.put(productDto.getName(),productDto);
        }
        List<ImooProductDto> imooProductDtoList = Lists.newArrayList();
        for (Map<String,String> map : list){
            if (StringUtils.isBlank(map.get("productName"))){
                continue;
            }
            ImooProductDto imooProductDto = new ImooProductDto();
            String productId = productMap.get(map.get("productName")).getId();
            imooProductDto.setImooPlantBasicProductId(map.get("id"));
            imooProductDto.setProductId(map.get("productName")==null?null:productId);
            imooProductDto.setCreatedBy(RequestUtils.getAccountId());
            imooProductDto.setCreatedDate(LocalDateTimeUtils.format(LocalDateTime.now()));
            imooProductDto.setLastModifiedBy(RequestUtils.getAccountId());
            imooProductDto.setLastModifiedDate(LocalDateTimeUtils.format(LocalDateTime.now()));
            imooProductDtoList.add(imooProductDto);
        }
        if (CollectionUtil.isNotEmpty(imooProductDtoList)){
            imooProductMapRepository.deleteAll();
            imooProductMapRepository.BatchSave(imooProductDtoList);
        }
    }
}
