package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.PricesystemDetailDto;
import net.myspring.future.modules.basic.repository.PricesystemDetailRepository;
import net.myspring.future.modules.basic.repository.PricesystemRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PricesystemDetailService {

    @Autowired
    private PricesystemDetailRepository pricesystemDetailRepository;
    @Autowired
    private PricesystemRepository pricesystemRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<List<Object>> findByProducts(String[] productIds){
        if(productIds.length == 0){
            return null;
        }
        List<List<Object>> pricesystemDetailDtoLists = Lists.newArrayList();
        Map<String,Product> productMap = CollectionUtil.extractToMap(productRepository.findAll(Arrays.asList(productIds)),"id");
        for(String productId:productIds){
            List<Pricesystem> pricesystems  =pricesystemRepository.findAllEnabled();
            List<PricesystemDetail> pricesystemDetails = pricesystemDetailRepository.findByProductId(productId);
            Map<String,PricesystemDetail> pricesystemDetailMap = CollectionUtil.extractToMap(pricesystemDetails,"pricesystemId");
            List<Object> rows = Lists.newArrayList();
            rows.add(productMap.get(productId).getName());
            for(Pricesystem pricesystem:pricesystems){
                if(pricesystemDetailMap.get(pricesystem.getId()) != null){
                    rows.add(pricesystemDetailMap.get(pricesystem.getId()).getPrice());
                }else{
                    rows.add(BigDecimal.ZERO);
                }
            }
            pricesystemDetailDtoLists.add(rows);
        }
        return pricesystemDetailDtoLists;

    }

}
