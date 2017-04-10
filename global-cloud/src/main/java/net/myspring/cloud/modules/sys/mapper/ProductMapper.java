package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.Product;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuj on 2017/4/5.
 */
@Mapper
public interface ProductMapper extends CrudMapper<Product,String> {

    List<Product> findByCompanyId(@Param("companyId") String companyId);

    LocalDateTime findMaxOutDate(@Param("companyId") String companyId);

    String findReturnOutId(@Param("companyId") String companyId);
}
