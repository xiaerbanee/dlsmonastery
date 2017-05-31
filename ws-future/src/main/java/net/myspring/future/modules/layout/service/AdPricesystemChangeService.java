package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.AdPricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.AdPricesystemDetailRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.repository.AdpricesystemRepository;
import net.myspring.future.modules.basic.web.query.ProductQuery;
import net.myspring.future.modules.layout.domain.AdPricesystemChange;
import net.myspring.future.modules.layout.dto.AdPricesystemChangeDto;
import net.myspring.future.modules.layout.repository.AdPricesystemChangeRepository;
import net.myspring.future.modules.layout.web.query.AdPricesystemChangeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdPricesystemChangeService {

    @Autowired
    private AdPricesystemChangeRepository adPricesystemChangeRepository;
    @Autowired
    private AdpricesystemRepository adpricesystemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AdPricesystemDetailRepository adPricesystemDetailRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public AdPricesystemChange findOne(String id){
        AdPricesystemChange adPricesystemChange=adPricesystemChangeRepository.findOne(id);
        return adPricesystemChange;
    }

    public Page<AdPricesystemChangeDto> findPage(Pageable pageable, AdPricesystemChangeQuery adPricesystemChangeQuery) {
        Page<AdPricesystemChangeDto> page = adPricesystemChangeRepository.findPage(pageable, adPricesystemChangeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<AdPricesystemChangeDto> findFilter(AdPricesystemChangeQuery adPricesystemChangeQuery) {
        ProductQuery productQuery = new ProductQuery();
        productQuery.setId(adPricesystemChangeQuery.getProductId());

        List<AdPricesystemChangeDto> adPricesystemChangeDtos = adPricesystemChangeRepository.findFilter(adPricesystemChangeQuery);
        cacheUtils.initCacheInput(adPricesystemChangeDtos);
        return adPricesystemChangeDtos;
    }

    public void save(List<List<String>> data){
        int pricesystemFromIndex = 5;
        List<AdPricesystem> adPricesystems = adpricesystemRepository.findList(null);
        List<Map<String, AdPricesystemDetail>> adPricesystemList = Lists.newArrayList();
        for (AdPricesystem adPricesystem : adPricesystems) {
            Map<String, AdPricesystemDetail> map = Maps.newHashMap();
            List<AdPricesystemDetail> adPricesystemDetailList = adPricesystemDetailRepository.findByAdPricesystemId(adPricesystem.getId());
            if (CollectionUtil.isNotEmpty(adPricesystemDetailList)) {
                for (AdPricesystemDetail adPricesystemDetail : adPricesystemDetailList) {
                    map.put(adPricesystemDetail.getProductId(), adPricesystemDetail);
                }
            }
            adPricesystemList.add(map);
        }
        for (List<String> row : data) {
            Product product = productRepository.findOne(StringUtils.toString(row.get(0)).trim());
            for (int i = 0; i < row.size(); i++) {
                String value = StringUtils.toString(row.get(i)).trim();
                switch (i) {
                    case 0:
                    case 1:
                    case 2:
                        break;
                    case 3:
                        if (StringUtils.isBlank(value)) {
                            product.setVolume(null);
                        } else {
                            product.setVolume(new BigDecimal(value));
                        }
                        break;
                    case 4:
                        if (StringUtils.isBlank(value)) {
                            product.setShouldGet(null);
                        } else {
                            product.setShouldGet(new BigDecimal(value));
                        }
                        break;
                    default:
                        AdPricesystem adPricesystem = adPricesystems.get(i - pricesystemFromIndex);
                        Map<String, AdPricesystemDetail> map = adPricesystemList.get(i - pricesystemFromIndex);
                        BigDecimal price = null;
                        if (StringUtils.isNotBlank(value)) {
                            price = new BigDecimal(value);
                        }
                        AdPricesystemDetail adPricesystemDetail = map.get(product.getId());
                        if (adPricesystemDetail == null) {
                            adPricesystemDetail = new AdPricesystemDetail();
                            adPricesystemDetail.setAdPricesystemId(adPricesystem.getId());
                            adPricesystemDetail.setProductId(product.getId());
                        }
                        if ((adPricesystemDetail.getPrice() == null && price != null) || (adPricesystemDetail.getPrice() != null && price == null) || (adPricesystemDetail.getPrice() != null && price != null && adPricesystemDetail.getPrice().compareTo(price) != 0)) {
                            AdPricesystemChange adPricesystemChange = new AdPricesystemChange();
                            adPricesystemChangeRepository.save(adPricesystemChange);
                        }
                        adPricesystemDetail.setPrice(price);
                        adPricesystemDetailRepository.save(adPricesystemDetail);
                        break;
                }
            }
            productRepository.save(product);
        }
    }


}
