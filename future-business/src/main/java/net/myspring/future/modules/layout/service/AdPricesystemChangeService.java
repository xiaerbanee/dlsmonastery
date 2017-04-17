package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.AdPricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.AdPricesystemDetailMapper;
import net.myspring.future.modules.basic.mapper.AdPricesystemMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.layout.domain.AdPricesystemChange;
import net.myspring.future.modules.layout.mapper.AdPricesystemChangeMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class AdPricesystemChangeService {

    @Autowired
    private AdPricesystemChangeMapper adPricesystemChangeMapper;
    @Autowired
    private AdPricesystemMapper adPricesystemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private AdPricesystemDetailMapper adPricesystemDetailMapper;

    public AdPricesystemChange findOne(String id){
        AdPricesystemChange adPricesystemChange=adPricesystemChangeMapper.findOne(id);
        return adPricesystemChange;
    }

    public Page<AdPricesystemChange> findPage(Pageable pageable, Map<String, Object> map) {
        Page<AdPricesystemChange> page = adPricesystemChangeMapper.findPage(pageable, map);
        return page;
    }

    public List<AdPricesystemChange> findFilter(Map<String, Object> map) {
        List<AdPricesystemChange> page = adPricesystemChangeMapper.findFilter(map);
        return page;
    }

    public void save(List<List<String>> data){
        int pricesystemFromIndex = 5;
        List<AdPricesystem> adPricesystems = adPricesystemMapper.findFilter(null);
        List<Map<String, AdPricesystemDetail>> adPricesystemList = Lists.newArrayList();
        for (AdPricesystem adPricesystem : adPricesystems) {
            Map<String, AdPricesystemDetail> map = Maps.newHashMap();
            List<AdPricesystemDetail> adPricesystemDetailList = adPricesystemDetailMapper.findByAdPricesystemId(adPricesystem.getId());
            if (CollectionUtil.isNotEmpty(adPricesystemDetailList)) {
                for (AdPricesystemDetail adPricesystemDetail : adPricesystemDetailList) {
                    map.put(adPricesystemDetail.getProductId(), adPricesystemDetail);
                }
            }
            adPricesystemList.add(map);
        }
        for (List<String> row : data) {
            Product product = productMapper.findOne(StringUtils.toString(row.get(0)).trim());
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
                            adPricesystemChangeMapper.save(adPricesystemChange);
                        }
                        adPricesystemDetail.setPrice(price);
                        adPricesystemDetailMapper.save(adPricesystemDetail);
                        break;
                }
            }
            productMapper.save(product);
        }
    }


}
