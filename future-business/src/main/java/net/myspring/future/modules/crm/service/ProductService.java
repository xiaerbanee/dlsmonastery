package net.myspring.future.modules.crm.service;

import com.google.common.collect.Maps;
import net.myspring.future.modules.crm.domain.AdApply;
import net.myspring.future.modules.crm.domain.Product;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.mapper.DepotMapper;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import net.myspring.future.modules.crm.mapper.ProductMapper;
import net.myspring.future.modules.crm.mapper.ProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ProductImeMapper productImeMapper;

    public Page<Product> findPage(Pageable pageable, Map<String, Object> map) {
        Page<Product> page = productMapper.findPage(pageable, map);
        return page;
    }

    public List<Product> findFilter(Map<String,Object> map){
        List<Product> productList = productMapper.findFilter(map);
        return productList;
    }

    public Map<String,Integer> getproductNumber(List<String> imeList){
        Map<String,Integer> productMap=Maps.newHashMap();
        List<ProductIme> productImes=productImeMapper.findByImeList(imeList);
        for(ProductIme productIme:productImes){
            String productName=productIme.getProduct().getName();
            if(!productMap.containsKey(productName)){
                productMap.put(productName,0);
            }
            productMap.put(productName,productMap.get(productName)+1);
        }
        return productMap;
    }

    public List<Product> findByIds(List<String> ids) {
        return productMapper.findByIds(ids);
    }

    public Product findByName(String name){
        return productMapper.findByName(name);
    }

    public List<Product> findByNameLikeHasIme(String name){
        return productMapper.findByNameLikeHasIme(name);
    }

    public List<Product> findByCodeLikeHasIme(String code){
        return productMapper.findByCodeLikeHasIme(code);
    }

    public List<Product> findByNameLike(String name){
        return productMapper.findByNameLike(name);
    }

    public List<Product> findByCodeLike(String code){
        return productMapper.findByCodeLike(code);
    }

    public Product findOne(String id) {
        Product product=productMapper.findOne(id);
        return product;
    }

    public List<Product> findHasImeProduct(){
        List<Product> productList=productMapper.findHasImeProduct();
        return productList;
    }

    public List<Product> findByOutGroupIds(List<String> outGroupIds){
        List<Product> productList = productMapper.findByOutGroupIds(outGroupIds);
        return productList;
    }

    public  List<Product> findAdProduct(String billType, AdApply adApply){
        return null;
    }

    public Product save(Product product) {
        productMapper.update(product);
        return product;
    }

    @Transactional
    public void syn() {

    }
}
