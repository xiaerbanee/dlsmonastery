package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.Product;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by liuj on 2017/4/5.
 */
@Mapper
public interface ProductMapper extends CrudMapper<Product,String> {
}
