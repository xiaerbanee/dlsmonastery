package net.myspring.future.modules.third.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.third.client.ImooClient;
import net.myspring.future.modules.third.dto.ImooPrdocutImeiDeliver;
import net.myspring.future.modules.third.dto.ImooProductDto;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ImooService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ImooClient imooClient;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private ProductRepository productRepository;


    public String pullFactoryData(String date){
        String dateStart = LocalDateUtils.format(LocalDateUtils.parse(date).minusDays(7));
        String dateEnd = LocalDateUtils.format(LocalDateUtils.parse(date).plusDays(1));
        String agentCodes = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
        Map<String,ImooProductDto> map = imooClient.getImooProductDtoMap();
        List<ImooPrdocutImeiDeliver> imooPrdocutImeiDeliverList = imooClient.getSendImeList(dateStart,dateEnd,agentCodes);
        String defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue();
        List<ProductIme> productImeList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(imooPrdocutImeiDeliverList)){
            List<String> updateIdList = Lists.newArrayList();
            for (ImooPrdocutImeiDeliver imooPrdocutImeiDeliver :imooPrdocutImeiDeliverList){
                if (map.containsKey(imooPrdocutImeiDeliver.getMsiItem())){
                    ImooProductDto imooProductDto = map.get(imooPrdocutImeiDeliver.getMsiItem());
                    ProductIme productIme = productImeRepository.findByEnabledIsTrueAndIme(imooPrdocutImeiDeliver.getImei());
                    if (productIme==null){
                        productIme = new ProductIme();
                        productIme.setDepotId(defaultStoreId);
                        productIme.setRetailShopId(defaultStoreId);
                        productIme.setIme(imooPrdocutImeiDeliver.getImei());
                        productIme.setIme2(imooPrdocutImeiDeliver.getMsibBarcode());
                        productIme.setItemNumber(imooPrdocutImeiDeliver.getMsiItem());
                        productIme.setImeReverse(StringUtils.reverse(imooPrdocutImeiDeliver.getImei()));
                        productIme.setColorId(null);
                        productIme.setCreatedTime(imooPrdocutImeiDeliver.getCreationDate());
                        productIme.setBoxIme(imooPrdocutImeiDeliver.getBox());
                        productIme.setInputType("工厂入库");
                        productIme.setProductId(imooProductDto.getProductId());
                        productIme.setBillId(null);
                        productImeList.add(productIme);
                        //更新product 中HasiMe 为true
                        updateIdList.add(imooProductDto.getProductId());
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(productImeList)){
                productImeRepository.batchSave(productImeList);
            }
            if (CollectionUtil.isNotEmpty(updateIdList)){
                productRepository.updateHasImeById(updateIdList);
            }
        }
        return "同步成功";
    }

}
