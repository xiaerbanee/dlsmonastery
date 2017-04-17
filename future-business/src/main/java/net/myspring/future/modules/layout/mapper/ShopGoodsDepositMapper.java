package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShopGoodsDepositMapper extends MyMapper<ShopGoodsDeposit,String> {

    Page<ShopGoodsDeposit> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<ShopGoodsDeposit> findByShopId(@Param("shopId") String shopId, @Param("status") String status);

}
