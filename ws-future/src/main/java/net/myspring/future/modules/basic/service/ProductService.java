package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.BoolEnum;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.util.text.IdUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.basic.web.form.ProductForm;
import net.myspring.future.modules.basic.web.query.ProductQuery;
import net.myspring.future.modules.layout.web.form.AdApplyForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
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
    private ProductMapper productMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    

    public Page<ProductDto> findPage(Pageable pageable, ProductQuery productQuery) {
        Page<ProductDto> page = productMapper.findPage(pageable, productQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<ProductDto> findFilter(ProductQuery productQuery){
        List<Product> productList = productMapper.findFilter(productQuery);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public List<Product> findByIds(List<String> ids) {
        return productMapper.findByIds(ids);
    }

    public Product findByName(String name){
        return productMapper.findByName(name);
    }

    public List<Product> findByOutName(){
        return productMapper.findByOutName();
    }

    public List<ProductDto> findByNameLikeHasIme(String name){
        List<Product> productList=productMapper.findByNameLikeHasIme(name);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public List<ProductDto> findByCodeLikeHasIme(String code){
        List<Product> productList=productMapper.findByCodeLikeHasIme(code);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public List<ProductDto> findByNameLike(String name){
        List<Product> productList=productMapper.findByNameLike(name);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public List<ProductDto> findByCodeLike(String code){
        List<Product> productList=productMapper.findByCodeLike(code);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public Product findOne(String id) {
        Product product=productMapper.findOne(id);
        return product;
    }

    public List<ProductDto> findHasImeProduct(){
        List<Product> productList=productMapper.findHasImeProduct();
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public List<ProductDto> findByOutGroupIds(List<String> outGroupIds){
        List<Product> productList = productMapper.findByOutGroupIds(outGroupIds);
        List<ProductDto> productDtoList= BeanUtil.map(productList,ProductDto.class);
        return productDtoList;
    }

    public  List<Product> findAdProduct(String billType,AdApplyForm adApplyForm){
        List<String> outGroupIds =Lists.newArrayList();
        if(BillTypeEnum.POP.name().equals(billType)){
            String value = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.PRODUCT_POP_GROUP_IDS.name()).getValue();
            outGroupIds = IdUtils.getIdList(value);
        }else if (BillTypeEnum.配件赠品.name().equals(billType)){
            String value = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.PRODUCT_GOODS_POP_GROUP_IDS.name()).getValue();
            outGroupIds = IdUtils.getIdList(value);
        }
        List<Product> adProducts  = productMapper.findByOutGroupIds(outGroupIds);
        if(adApplyForm.getShopId() != null){
            Depot depot = depotMapper.findOne(adApplyForm.getShopId());
            adApplyForm.setShop(depot);
        }
        return adProducts;
    }

    public Product save(ProductForm productForm) {
        Product product= productMapper.findOne(productForm.getId());
        ReflectionUtil.copyProperties(productForm,product);
        productMapper.update(product);
        return product;
    }

    public ProductForm getForm(ProductForm productForm){
        if(!productForm.isCreate()){
            Product product=productMapper.findOne(productForm.getId());
            productForm=BeanUtil.map(product,ProductForm.class);
            cacheUtils.initCacheInput(productForm);
        }
        return productForm;
    }

    public void syn() {
        LocalDateTime dateTime=productMapper.getMaxOutDate();
        String cloudName = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.CLOUD_DB_NAME.name()).getValue();
        String result = cloudClient.getSynProductData(cloudName, LocalDateTimeUtils.format(dateTime));
        String value = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.PRODUCT_GOODS_GROUP_IDS.name()).getValue();
        List<String> outGroupIds = IdUtils.getIdList(value);
        List<Map<String, Object>> dataList = ObjectMapperUtils.readValue(result,List.class);
        if(CollectionUtil.isNotEmpty(dataList)) {
            for (Map<String, Object> map : dataList) {
                Product product = productMapper.findByOutId(map.get("outId").toString());
                if(product==null) {
                    product = productMapper.findByName(map.get("name").toString());
                    if(product ==null) {
                        product = new Product();
                        product.setAllowBill(false);
                        product.setAllowOrder(false);
                        if(CollectionUtil.isNotEmpty(outGroupIds) && outGroupIds.contains(map.get("fgroup").toString())) {
                            product.setHasIme(true);
                        }else {
                            product.setHasIme(false);
                        }
                        productMapper.save(product);
                    }
                }
                product.setOutDate(LocalDateTimeUtils.parse(map.get("modifyDate").toString()));
                product.setName(map.get("name").toString());
                product.setOutId(map.get("outId").toString());
                product.setOutGroupId(map.get("fgroup").toString());
                product.setOutGroupName(map.get("groupName").toString());
                product.setCode(map.get("code").toString());
                productMapper.update(product);

            }
        }
    }

    public List<String> findNetTypeList(){
        return NetTypeEnum.getList();
    }

    public Map<String,String> getMap(){
        return BoolEnum.getMap();
    }

    public List<ProductType> findProductTypeList() {
        return productMapper.findProductTypeList();
    }

    public void delete(ProductDto productDto) {
        productMapper.logicDeleteOne(productDto.getId());
    }

    public List<ProductDto> findIntersectionOfBothPricesystem(String pricesystemId1, String pricesystemId2) {
        return  productMapper.findIntersectionOfBothPricesystem(pricesystemId1, pricesystemId2);
    }
}
