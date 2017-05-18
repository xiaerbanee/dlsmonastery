package net.myspring.future.modules.crm.service;

import com.google.common.collect.Maps;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.dto.ProductImeHistoryDto;
import net.myspring.future.modules.crm.mapper.*;
import net.myspring.future.modules.crm.web.query.ProductImeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductImeService {
    @Autowired
    private ProductImeMapper productImeMapper;
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ImeAllotMapper imeAllotMapper;
    @Autowired
    private GoodsOrderImeMapper goodsOrderImeMapper;

    @Autowired
    private StoreAllotImeMapper storeAllotImeMapper;

    @Autowired
    private ProductImeUploadMapper productImeUploadMapper;

    @Autowired
    private ProductImeSaleMapper productImeSaleMapper;





    @Autowired
    private CacheUtils cacheUtils;

    //分页，但不查询总数
    public Page<ProductImeDto> findPage(Pageable pageable,ProductImeQuery productImeQuery) {
        productImeQuery.setPageable(pageable);
        List<ProductImeDto> productImeDtoList = productImeMapper.findList(productImeQuery);

        cacheUtils.initCacheInput(productImeDtoList);
        Page<ProductImeDto> page = new PageImpl(productImeDtoList,pageable,(pageable.getPageNumber()+100)*pageable.getPageSize());
        return page;
    }


    public List<ProductImeDto> findByImeList(List<String> imeList){
        List<ProductIme> productImeList=productImeMapper.findByImeList(imeList);
        List<ProductImeDto> productImeDtoList= BeanUtil.map(productImeList,ProductImeDto.class);
        cacheUtils.initCacheInput(productImeDtoList);
        return productImeDtoList;
    }

    public Map<String,Integer> findQtyMap(List<String> imeList){
        Map<String,Integer> map= Maps.newHashMap();
        List<ProductIme> productImeList=productImeMapper.findByImeList(imeList);
        if(CollectionUtil.isNotEmpty(productImeList)){
            List<Product> productList=productMapper.findByIds(CollectionUtil.extractToList(productImeList,"productId"));
            Map<String,Product> productMap=CollectionUtil.extractToMap(productList,"id");
            Map<String,List<ProductIme>> productImeMap=CollectionUtil.extractToMapList(productImeList,"productId");
            for(Map.Entry<String,List<ProductIme>> entry:productImeMap.entrySet()){
                map.put(productMap.get(entry.getKey()).getName(),entry.getValue().size());
            }
        }
        return map;
    }

    public ProductImeDto getProductImeDetail(String id) {
        return productImeMapper.getProductImeDetail(id);

    }

    public List<ProductImeHistoryDto> getProductImeHistoryList(String productImeId) {

        List<ProductImeHistoryDto> list = productImeMapper.getProductImeHistoryList(productImeId);
        cacheUtils.initCacheInput(list);

        return list;
    }
}
