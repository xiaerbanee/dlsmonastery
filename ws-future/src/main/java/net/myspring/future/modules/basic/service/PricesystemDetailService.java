package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.PricesystemDetailDto;
import net.myspring.future.modules.basic.repository.PricesystemDetailRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PricesystemDetailService {

    @Autowired
    private PricesystemDetailRepository pricesystemDetailRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<List<Object>> findByProducts(String[] productIds){
        if(productIds.length == 0){
            return null;
        }
        List<List<Object>> pricesystemDetailDtoLists = Lists.newArrayList();
        Map<String,Product> productMap = CollectionUtil.extractToMap(productRepository.findAll(Arrays.asList(productIds)),"id");
        for(String productId:productIds){
            List<PricesystemDetail> pricesystemDetails = pricesystemDetailRepository.findByProductId(productId);
            List<Object> rows = Lists.newArrayList();
            rows.add(productMap.get(productId).getName());
            for(PricesystemDetail pricesystemDetail:pricesystemDetails){
                rows.add(pricesystemDetail.getPrice());
            }
            pricesystemDetailDtoLists.add(rows);
        }
        return pricesystemDetailDtoLists;

    }

}
