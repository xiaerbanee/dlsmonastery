package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.future.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.web.query.ProductQuery;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
@CacheDefaults(cacheName = "products")
public interface ProductMapper extends BaseMapper<Product,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Product findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Product> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Product product);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Product product);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Product> findAllEnabled();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_BY_IDS)
    List<Product> findByIds(List<String> ids);
    
    List<Product> findHasImeProduct();

    List<Product> findByNameLike(String name);

    List<Product> findByCodeLike(String code);

    List<Product> findByNameLikeHasIme(String name);

    List<Product> findByCodeLikeHasIme(String code);

    Product findByName(String name);

    Product findByOutId(String outId);

    Page<ProductDto> findPage(Pageable pageable, @Param("p")ProductQuery productQuery);

    List<Product> findFilter(@Param("p") ProductQuery productQuery);

    List<Product> findByOutName();

    List<Product> findByOutGroupIds(List<String> outGroupIds);

    List<Product> findByProductTypeId(String productTypeId);

    int updateProductTypeId(@Param("productTypeId") String id, @Param("list") List<String> ids);

    int updateProductTypeToNull(String productTypeId);

    List<Product> findLabels(List<String> ids);

    List<ProductDto> findByOutGroupIdsAndAllowOrder(@Param("outGroupIds") List<String> outGroupIds, @Param("allowOrder") boolean allowOrder);

    LocalDateTime getMaxOutDate();

    List<ProductType> findProductTypeList();

    List<ProductDto> findIntersectionOfBothPricesystem(@Param("pricesystemId1")  String pricesystemId1, @Param("pricesystemId2") String pricesystemId2);

    List<Product> findByNameList(List<String> nameList);
}
