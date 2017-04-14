package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper extends MyMapper<Product,String> {

    List<Product> findHasImeProduct();

    List<Product> findByNameLike(String name);

    List<Product> findByCodeLike(String code);

    List<Product> findByNameLikeHasIme(String name);

    List<Product> findByCodeLikeHasIme(String code);

    Product findByName(String name);

    Product findByOutId(String outId);

    Page<Product> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<Product> findFilter(@Param("p") Map<String, Object> map);

    List<Product> findByOutName();

    List<Product> findByOutGroupIds(List<String> outGroupIds);

    List<Product> findByProductTypeIds(List<Long> productTypeIds);

    int updateProductTypeId(@Param("productTypeId") String id, @Param("list") List<String> ids);

    int updateProductTypeToNull(String productTypeId);

    List<Product> findLabels(List<String> ids);

    List<Product> findByOutGroupIdsAndAllowOrder(@Param("outGroupIds") List<String> outGroupIds, @Param("allowOrder") boolean allowOrder);

    LocalDateTime getMaxOutDate();
}
