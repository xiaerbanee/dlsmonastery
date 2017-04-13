package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductImeUploadMapper extends MyMapper<ProductImeUpload,String> {

    Page<ProductImeUpload> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<ProductImeUpload> findByProductImeId(@Param("productImeId") String productImeId);
}
