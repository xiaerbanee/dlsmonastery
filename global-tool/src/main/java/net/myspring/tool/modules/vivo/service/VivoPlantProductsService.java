package net.myspring.tool.modules.vivo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.tool.common.utils.CacheUtils;
import net.myspring.tool.modules.future.dto.ProductDto;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.dto.VivoPlantProductsDto;
import net.myspring.tool.modules.vivo.repository.VivoPlantProductsRepository;
import net.myspring.tool.modules.vivo.web.query.VivoPlantProductsQuery;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VivoPlantProductsService {

    @Autowired
    private VivoPlantProductsRepository vivoPlantProductsRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public List<VivoPlantProductsDto> findAll(VivoPlantProductsQuery vivoPlantProductsQuery){
        vivoPlantProductsQuery.setItemNumberList(StringUtils.getSplitList(vivoPlantProductsQuery.getItemNumberStr(), CharConstant.ENTER));
        List<VivoPlantProductsDto> vivoPlantProductsDtoList = vivoPlantProductsRepository.findAll(vivoPlantProductsQuery);
        cacheUtils.initCacheInput(vivoPlantProductsDtoList);
        return vivoPlantProductsDtoList;
    }

    public void save(List<ProductDto> productDtoList,String data){
        List<Map<String,String>> list = ObjectMapperUtils.readValue(data, ArrayList.class);
        Map<String,ProductDto> productMap= Maps.newHashMap();
        for(ProductDto productDto:productDtoList){
            productMap.put(productDto.getName(),productDto);
        }
        List<VivoPlantProducts> vivoPlantProducts=vivoPlantProductsRepository.findAll();
        Map<String,VivoPlantProducts> vivoPlantProductsMap =Maps.newHashMap();
        for(VivoPlantProducts vivoPlantProduct:vivoPlantProducts){
            vivoPlantProductsMap.put(vivoPlantProduct.getId(),vivoPlantProduct);
        }
        List<VivoPlantProducts> vivoPlantProductsList = Lists.newArrayList();
        for(Map<String,String> map:list){
            VivoPlantProducts vivoPlantProduct = vivoPlantProductsMap.get(map.get("id"));
            ProductDto defaultProduct=productMap.get(map.get("productName"));
            vivoPlantProduct.setProductId(defaultProduct==null?null:defaultProduct.getId());
            vivoPlantProductsList.add(vivoPlantProduct);
        }
        vivoPlantProductsRepository.save(vivoPlantProductsList);
    }


}
