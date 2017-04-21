package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.CompanyNameEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.IdUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.client.CompanyConfigClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.manager.ProductManager;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.basic.mapper.ProductTypeMapper;
import net.myspring.future.modules.basic.web.Query.ProductQuery;
import net.myspring.future.modules.basic.web.form.ProductForm;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import net.myspring.future.modules.layout.domain.AdApply;
import net.myspring.future.modules.layout.web.form.AdApplyForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ProductImeMapper productImeMapper;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ProductManager productManager;

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
            String value = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.PRODUCT_POP_GROUP_IDS.getCode());
            outGroupIds = IdUtils.getIdList(value);
        }else if (BillTypeEnum.配件赠品.name().equals(billType)){
            String value = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.PRODUCT_GOODS_POP_GROUP_IDS.getCode());
            outGroupIds = IdUtils.getIdList(value);
        }
        List<Product> adProducts  = productMapper.findByOutGroupIds(outGroupIds);
        if(adApplyForm.getShopId() != null){
            Depot depot = depotMapper.findOne(adApplyForm.getShopId());
            adApplyForm.setShop(depot);
            if(CompanyNameEnum.JXDJ.name().equals(SecurityUtils.getCompanyName())){
                if(depot != null && depot.getCode().startsWith("IM0")){
                    for(int i= adProducts.size()-1;i >= 0; i--){
                        Product product = adProducts.get(i);
                        if(!product.getOutGroupName().startsWith("imoo")){
                            adProducts.remove(i);
                        }
                    }
                }
                if(depot != null && depot.getCode().startsWith("DJ")){
                    for(int i = adProducts.size()-1; i>=0; i--){
                        Product product = adProducts.get(i);
                        if(!product.getOutGroupName().startsWith("电玩")){
                            adProducts.remove(i);
                        }
                    }
                }
            }
        }
        return adProducts;
    }

    public Product save(ProductForm productForm) {
        Product product=productManager.updateForm(productForm);
        return product;
    }

    public ProductForm findForm(ProductForm productForm){
        if(!productForm.isCreate()){
            Product product=productManager.findOne(productForm.getId());
            productForm=BeanUtil.map(product,ProductForm.class);
            cacheUtils.initCacheInput(productForm);
        }
        return productForm;
    }

    public void syn() {
        LocalDateTime dateTime=productMapper.getMaxOutDate();
        String cloudName = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.CLOUD_DB_NAME.getCode());
        String result = cloudClient.getSynProductData(cloudName, LocalDateTimeUtils.format(dateTime));
        String value = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.PRODUCT_GOODS_GROUP_IDS.getCode());
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
                if(CompanyNameEnum.JXVIVO.name().equals(SecurityUtils.getCompanyName()) || CompanyNameEnum.JXOPPO.name().equals(SecurityUtils.getCompanyName())){
                    if("商品类".equals(map.get("groupName").toString())){
                        if(map.get("name").toString().trim().contains("移动")){
                            product.setNetType("移动");
                        }else{
                            product.setNetType("联信");
                        }
                    }else{
                        product.setNetType("全网通");
                    }
                }else{
                    product.setNetType("全网通");
                }
                productMapper.update(product);

            }
        }
    }
}
