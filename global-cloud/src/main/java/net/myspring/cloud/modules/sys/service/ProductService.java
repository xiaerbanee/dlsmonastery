package net.myspring.cloud.modules.sys.service;

import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.DynamicDataSourceContext;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.utils.HandSonTableUtils;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.sys.domain.Product;
import net.myspring.cloud.modules.sys.dto.ProductDto;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import net.myspring.cloud.modules.sys.mapper.ProductMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static net.myspring.util.mapper.BeanUtil.map;

/**
 * Created by liuj on 2017/4/5.
 */
@Service
@LocalDataSource
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private KingdeeBookMapper kingdeeBookMapper;

    public LocalDateTime findMaxOutDate(String companyId){
        return productMapper.findMaxOutDate(companyId);
    }

    public Map<String,Object> getFormProperty(){
        String companyId = DynamicDataSourceContext.get().getCompanyId();
        List<Product> productList = productMapper.findByCompanyId(companyId);
        List<ProductDto> productDtoList = BeanUtil.map(productList,ProductDto.class);
        Map<String,Object> map= Maps.newHashMap();
        map.put("productNames", CollectionUtil.extractToList(productList,"name"));

        Map<String,String> productMap = Maps.newHashMap();
        for(ProductDto productDto : productDtoList){
            productMap.put(productDto.getName(),productDto.getCode());
        }
        map.put("productMap", productMap);
        return map;
    }

    public void save(List<List<Object>> datas) {
        String companyId = DynamicDataSourceContext.get().getCompanyId();
        List<Product> productList = productMapper.findByCompanyId(companyId);
        Map<String,Product> productMap = CollectionUtil.extractToMap(productList,"code");
        for (List<Object> row : datas) {
            String code = HandSonTableUtils.getValue(row,1);
            String priceStr = HandSonTableUtils.getValue(row,2);
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            if(productMap.get(code) != null){
                Product product = productMap.get(code);
                product.setPrice1(price);
                productMapper.update(product);
            }
        }
    }

    public void syn(List<BdMaterial> bdMaterials) {
        if (CollectionUtil.isNotEmpty(bdMaterials)) {
            String companyId = DynamicDataSourceContext.get().getCompanyId();
            String returnOutId = "";
            if(!KingdeeNameEnum.JXDJ.name().equals(kingdeeBookMapper.findNameByCompanyId(companyId))){
                returnOutId = productMapper.findReturnOutId(companyId);
            }
            List<Product> productList = productMapper.findByCompanyId(companyId);
            Map<String,Product> productMap = CollectionUtil.extractToMap(productList,"outId");
            for (BdMaterial bdmaterial : bdMaterials) {
                Product product = productMap.get(bdmaterial.getFmasterId());
                if (product == null) {
                    product = new Product();
                    product.setCompanyId(companyId);
                    product.setName(bdmaterial.getFname());
                    product.setCode(bdmaterial.getFnumber());
                    product.setOutId(bdmaterial.getFmasterId());
                    product.setOutDate(bdmaterial.getFmodifyDate());
                    product.setReturnOutId(returnOutId);
                    productMapper.save(product);
                } else {
                    product.setCode(bdmaterial.getFnumber());
                    product.setOutDate(bdmaterial.getFmodifyDate());
                    product.setName(bdmaterial.getFname());
                    productMapper.update(product);
                }
            }
        }
    }
}
