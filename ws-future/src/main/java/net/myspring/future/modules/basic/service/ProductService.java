package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.BoolEnum;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.util.text.IdUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.web.form.ProductForm;
import net.myspring.future.modules.basic.web.query.ProductQuery;
import net.myspring.future.modules.layout.web.form.AdApplyForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    

    public Page<ProductDto> findPage(Pageable pageable, ProductQuery productQuery) {
        Page<ProductDto> page = productRepository.findPage(pageable, productQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<ProductDto> findFilter(ProductQuery productQuery){
        List<Product> productList = productRepository.findFilter(productQuery);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public List<Product> findByIds(List<String> ids) {
        return productRepository.findAll(ids);
    }

    public Product findByName(String name){
        return productRepository.findByName(name);
    }

    public List<ProductDto> findByOutName(){
        return productRepository.findByOutName();
    }

    public List<ProductDto> findByNameLikeHasIme(String name){
        List<Product> productList=productRepository.findByNameLikeHasIme(name);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public List<ProductDto> findByCodeLikeHasIme(String code){
        List<Product> productList=productRepository.findByCodeLikeHasIme(code);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public List<ProductDto> findByNameLike(String name){
        List<Product> productList=productRepository.findByNameLike(name);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public List<ProductDto> findByCodeLike(String code){
        List<Product> productList=productRepository.findByCodeLike(code);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public ProductDto findOne(String id) {
        ProductDto productDto = new ProductDto();
        if(StringUtils.isNotBlank(id)){
            Product product=productRepository.findOne(id);
            productDto = BeanUtil.map(product,ProductDto.class);
            cacheUtils.initCacheInput(productDto);
        }
        return productDto;
    }

    public List<ProductDto> findHasImeProduct(){
        List<Product> productList=productRepository.findHasImeProduct();
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public List<ProductDto> findByOutGroupIds(List<String> outGroupIds){
        List<Product> productList = productRepository.findByOutGroupIds(outGroupIds);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public  List<Product> findAdProduct(String billType,AdApplyForm adApplyForm){
        List<String> outGroupIds =Lists.newArrayList();
        if(BillTypeEnum.POP.name().equals(billType)){
            String value = CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.PRODUCT_POP_GROUP_IDS.name()).getValue();
            outGroupIds = IdUtils.getIdList(value);
        }else if (BillTypeEnum.配件赠品.name().equals(billType)){
            String value = CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.PRODUCT_GOODS_POP_GROUP_IDS.name()).getValue();
            outGroupIds = IdUtils.getIdList(value);
        }
        List<Product> adProducts  = productRepository.findByOutGroupIds(outGroupIds);
        if(adApplyForm.getShopId() != null){
            Depot depot = depotRepository.findOne(adApplyForm.getShopId());
            //adApplyForm.setShop(depot);
        }
        return adProducts;
    }

    public void save(ProductForm productForm) {
            Product product= productRepository.findOne(productForm.getId());
            ReflectionUtil.copyProperties(productForm,product);
            productRepository.save(product);
    }

    public ProductQuery getQuery(ProductQuery productQuery){
        productQuery.setNetTypeList(NetTypeEnum.getList());
        productQuery.setOutGroupNameList(productRepository.findByOutName());
        return productQuery;
    }

    public ProductForm getForm(ProductForm productForm){
        productForm.setNetTypeList(NetTypeEnum.getList());
        return productForm;
    }

    public void syn() {
        LocalDateTime dateTime=productRepository.getMaxOutDate();
        String cloudName = CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.CLOUD_DB_NAME.name()).getValue();
        String result = cloudClient.getSynProductData(cloudName, LocalDateTimeUtils.format(dateTime));
        String value = CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.PRODUCT_GOODS_GROUP_IDS.name()).getValue();
        List<String> outGroupIds = IdUtils.getIdList(value);
        List<Map<String, Object>> dataList = ObjectMapperUtils.readValue(result,List.class);
        if(CollectionUtil.isNotEmpty(dataList)) {
            for (Map<String, Object> map : dataList) {
                Product product = productRepository.findByOutId(map.get("outId").toString());
                if(product==null) {
                    product = productRepository.findByName(map.get("name").toString());
                    if(product ==null) {
                        product = new Product();
                        product.setAllowBill(false);
                        product.setAllowOrder(false);
                        if(CollectionUtil.isNotEmpty(outGroupIds) && outGroupIds.contains(map.get("fgroup").toString())) {
                            product.setHasIme(true);
                        }else {
                            product.setHasIme(false);
                        }
                        productRepository.save(product);
                    }
                }
                product.setOutDate(LocalDateTimeUtils.parse(map.get("modifyDate").toString()));
                product.setName(map.get("name").toString());
                product.setOutId(map.get("outId").toString());
                product.setOutGroupId(map.get("fgroup").toString());
                product.setOutGroupName(map.get("groupName").toString());
                product.setCode(map.get("code").toString());
                productRepository.save(product);

            }
        }
    }


    public void delete(ProductDto productDto) {
        productRepository.logicDelete(productDto.getId());
    }

    public List<ProductDto> findIntersectionOfBothPricesystem(String pricesystemId1, String pricesystemId2) {
        return  productRepository.findIntersectionOfBothPricesystem(pricesystemId1, pricesystemId2);
    }
}
