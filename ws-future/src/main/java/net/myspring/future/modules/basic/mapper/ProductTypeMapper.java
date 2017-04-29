package net.myspring.future.modules.basic.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.basic.web.query.ProductTypeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductTypeMapper extends MyMapper<ProductType,String> {

    Page<ProductTypeDto> findPage(Pageable pageable, @Param("p") ProductTypeQuery productTypeQuery);

    List<ProductType> findByFilter(@Param("p") Map<String, Object> map);

    List<ProductType> findByDemoPhoneTypeIds(List<String> dempProductTypeIds);

    List<ProductType> findByNameLike(@Param("name") String name);

    List<ProductType> findLabels(List<String> ids);

    int updateDemoPhoneType(@Param("demoPhoneTypeId") String demoPhoneTypeId, @Param("list") List<String> ids);

    int updateDemoPhoneTypeToNull(String demoPhoneTypeId);

    List<ProductType> findAllScoreType();

}
