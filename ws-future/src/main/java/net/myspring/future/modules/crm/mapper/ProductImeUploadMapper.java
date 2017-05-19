package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.dto.ProductImeUploadDto;
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductImeUploadMapper extends MyMapper<ProductImeUpload,String> {

    List<ProductImeUploadDto> findList(@Param("p") ProductImeUploadQuery productImeUploadQuery);

    List<ProductImeUpload> findByProductImeId(@Param("productImeId") String productImeId);
}
