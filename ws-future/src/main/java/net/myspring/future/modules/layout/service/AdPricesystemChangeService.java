package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.AdPricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.AdPricesystemDto;
import net.myspring.future.modules.basic.repository.AdPricesystemDetailRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.repository.AdpricesystemRepository;
import net.myspring.future.modules.layout.domain.AdPricesystemChange;
import net.myspring.future.modules.layout.dto.AdPricesystemChangeDto;
import net.myspring.future.modules.layout.repository.AdPricesystemChangeRepository;
import net.myspring.future.modules.layout.web.query.AdPricesystemChangeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public List<AdPricesystemDto> findAdPricesystem(){
        return BeanUtil.map(adpricesystemRepository.findByEnabledIsTrue(),AdPricesystemDto.class);
    }

    public List<List<Object>> findFilter(AdPricesystemChangeQuery adPricesystemChangeQuery) {
        List<Product> productList;
        if(adPricesystemChangeQuery.getProductName()!=null){
            productList = productRepository.findByNameLike(adPricesystemChangeQuery.getProductName());
        }else{
            productList = productRepository.findAllEnabled();
        }
        List<AdPricesystem> adPricesystemList = adpricesystemRepository.findByEnabledIsTrue();
        List<List<Object>> data = getFormData(productList,adPricesystemList);
        return data;
    }

    public void save(String adpricesystemChanges){
        String json = HtmlUtils.htmlUnescape(adpricesystemChanges);
        Map<String,Product> productMap= CollectionUtil.extractToMap(productRepository.findAll(),"id");
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        List<AdPricesystem> adPricesystems = adpricesystemRepository.findByEnabledIsTrue();
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
        List<AdPricesystemDetail> adPricesystemDetails = Lists.newArrayList();
        List<AdPricesystemChange> adPricesystemChanges = Lists.newArrayList();
        List<Product> products = Lists.newArrayList();
        for (List<Object> row : data) {
            String productId = StringUtils.toString(row.get(0)).trim();
            for (int i = 0; i < row.size(); i++) {
                String value = StringUtils.toString(row.get(i)).trim();
                Product product = productMap.get(StringUtils.toString(row.get(0)).trim());
                switch (i) {
                    case 0:
                    case 1:
                    case 2:
                        break;
                    case 3:
                        if (StringUtils.isNotBlank(value)&&product.getVolume().compareTo(new BigDecimal(value))!=0) {
                            product.setVolume(new BigDecimal(value));
                        }
                        break;
                    case 4:
                        if (StringUtils.isNotBlank(value)&&product.getShouldGet().compareTo(new BigDecimal(value))!=0) {
                            product.setShouldGet(new BigDecimal(value));
                        }
                        break;
                    default:
                        AdPricesystem adPricesystem = adPricesystems.get(i - 5);
                        Map<String, AdPricesystemDetail> map = adPricesystemList.get(i - 5);
                        BigDecimal price = null;
                        if (StringUtils.isNotBlank(value)) {
                            price = new BigDecimal(value);
                        }
                        AdPricesystemDetail adPricesystemDetail = map.get(productId);
                        if (adPricesystemDetail == null) {
                            adPricesystemDetail = new AdPricesystemDetail();
                            adPricesystemDetail.setAdPricesystemId(adPricesystem.getId());
                            adPricesystemDetail.setProductId(productId);
                        }
                        if ((adPricesystemDetail.getPrice() == null && price != null) || (adPricesystemDetail.getPrice() != null && price == null) || (adPricesystemDetail.getPrice() != null && price != null && adPricesystemDetail.getPrice().compareTo(price) != 0)) {
                            AdPricesystemChange adPricesystemChange = new AdPricesystemChange();
                            adPricesystemChange.setOldPrice(adPricesystemDetail.getPrice());
                            adPricesystemChange.setNewPrice(price);
                            adPricesystemChange.setAdPricesystemId(adPricesystem.getId());
                            adPricesystemChange.setProductId(productId);
                            adPricesystemChanges.add(adPricesystemChange);
                        }
                        adPricesystemDetail.setPrice(price);
                        adPricesystemDetails.add(adPricesystemDetail);
                        break;
                }
                products.add(product);
            }
        }
        productRepository.save(products);
        adPricesystemChangeRepository.save(adPricesystemChanges);
        adPricesystemDetailRepository.save(adPricesystemDetails);
    }

    //拼接数据给界面
    private List<List<Object>> getFormData(List<Product> productList, List<AdPricesystem> adPricesystemList) {
        List<List<Object>> datas = Lists.newArrayList();
        List<Map<String, AdPricesystemDetail>> adPricesystemDetailList = Lists.newArrayList();
        for (AdPricesystem adPricesystem : adPricesystemList) {
            Map<String, AdPricesystemDetail> map = Maps.newHashMap();
            List<AdPricesystemDetail> adPricesystemDetails = adPricesystemDetailRepository.findByAdPricesystemId(adPricesystem.getId());
            if(adPricesystemDetails!=null){
                for (AdPricesystemDetail adPricesystemDetail : adPricesystemDetails) {
                    map.put(adPricesystemDetail.getProductId(), adPricesystemDetail);
                }
            }
            adPricesystemDetailList.add(map);
        }
        for (Product product : productList) {
            List<Object> rows = Lists.newArrayList();
            rows.add(product.getId());
            rows.add(product.getCode());
            rows.add(product.getName());
            rows.add(product.getVolume());
            rows.add(product.getShouldGet());
            for (int i = 0; i < adPricesystemList.size(); i++) {
                AdPricesystemDetail adPricesystemDetail = adPricesystemDetailList.get(i).get(product.getId());
                rows.add(adPricesystemDetail == null ? "" : adPricesystemDetail.getPrice());
            }
            datas.add(rows);
        }
        return datas;
    }

}
