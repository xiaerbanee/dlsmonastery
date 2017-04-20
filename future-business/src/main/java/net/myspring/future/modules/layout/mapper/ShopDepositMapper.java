package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShopDepositMapper extends MyMapper<ShopDeposit,String> {

    Page<ShopDeposit> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<ShopDeposit> findByTypeAndShopIds(@Param("type") String type, @Param("shopIds") List<String> shopIds);

    List<ShopDeposit> findByTypeAndShopId(@Param("type") String type, @Param("shopId") String shopId, @Param("size") Integer size);
}
