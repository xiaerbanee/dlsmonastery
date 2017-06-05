package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.sys.domain.Product;
import net.myspring.cloud.modules.sys.dto.ProductDto;
import net.myspring.cloud.modules.sys.repository.ProductRepository;
import net.myspring.cloud.modules.sys.web.form.ProductForm;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liuj on 2017/4/5.
 */
@Service
@LocalDataSource
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public LocalDateTime findMaxOutDate(String companyId){
        return productRepository.findMaxOutDate(companyId);
    }

    public ProductForm getForm(ProductForm productForm){
        String companyId = RequestUtils.getCompanyId();
        List<String> productList = productRepository.findNameByCompanyId(companyId);
        productForm.setProductNameList(productList);
        return productForm;
    }

    public ProductDto findByName(String name){
        String companyId = RequestUtils.getCompanyId();
        Product product = productRepository.findByNameAndCompanyId(companyId,name);
        ProductDto productDto = BeanUtil.map(product,ProductDto.class);
        return productDto;
    }

    public void save(ProductForm productForm) {
        String json = HtmlUtils.htmlUnescape(productForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        String companyId = RequestUtils.getCompanyId();
        List<Product> productList = productRepository.findByCompanyId(companyId);
        Map<String,Product> productMap = productList.stream().collect(Collectors.toMap(Product::getCode, Product->Product));
        for (List<Object> row : data) {
            String code = HandsontableUtils.getValue(row,1);
            String priceStr = HandsontableUtils.getValue(row,2);
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            if(productMap.get(code) != null){
                Product product = productMap.get(code);
                product.setPrice1(price);
                productRepository.save(product);
            }
        }
    }

}
