package net.myspring.future.modules.crm.mapper;

import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by liuj on 2017/4/2.
 */
@Mapper
public interface ProductImeMapper extends CrudMapper<ProductIme,String>{

    Page<ProductIme> findPage(Pageable pageable);

}
