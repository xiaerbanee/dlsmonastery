package net.myspring.future.modules.layout.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.dto.ShopDepositDto;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.future.modules.layout.web.query.ShopDepositQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ShopDepositMapper extends MyMapper<ShopDeposit,String> {

    Page<ShopDepositDto> findPage(Pageable pageable, @Param("p") ShopDepositQuery shopDepositQuery);

    List<ShopDeposit> findByTypeAndShopIds(@Param("type") String type, @Param("shopIds") List<String> shopIds);

    List<ShopDeposit> findByTypeAndShopId(@Param("type") String type, @Param("shopId") String shopId, @Param("size") Integer size);

}
