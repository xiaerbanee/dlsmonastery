package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;
import net.myspring.future.modules.layout.dto.ShopGoodsDepositDto;
import net.myspring.future.modules.layout.web.query.ShopGoodsDepositQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ShopGoodsDepositMapper extends MyMapper<ShopGoodsDeposit,String> {

    Page<ShopGoodsDepositDto> findPage(Pageable pageable, @Param("p") ShopGoodsDepositQuery shopGoodsDepositQuery);

    List<ShopGoodsDeposit> findByShopId(@Param("shopId") String shopId, @Param("status") String status);

}
