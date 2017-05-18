package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopAllotDetailMapper extends MyMapper<ShopAllotDetail,String> {

    List<ShopAllotDetail> findByShopAllotId(String shopAllotId);

    List<ShopAllotDetail> findByShopAllotIds(List<String> shopAllotIdList);

    int deleteByShopAllotId(@Param("shopAllotId") String shopAllotId);

    List<ShopAllotDetailDto> getShopAllotDetailListForNewOrEdit(@Param("shopAllotId")  String shopAllotId, @Param("fromDepotId")String fromDepotId, @Param("toDepotId")String toDepotId);

    List<ShopAllotDetailDto> getShopAllotDetailListForViewOrAudit(@Param("shopAllotId") String shopAllotId);
}
