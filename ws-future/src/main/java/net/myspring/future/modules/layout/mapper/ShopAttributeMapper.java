package net.myspring.future.modules.layout.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopAttributeMapper extends MyMapper<ShopAttribute,String> {

    List<ShopAttribute> findByShopId(@Param("shopId") String shopId);
}
