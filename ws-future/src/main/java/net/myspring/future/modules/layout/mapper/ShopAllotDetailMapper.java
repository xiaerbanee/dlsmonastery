package net.myspring.future.modules.layout.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopAllotDetailMapper extends MyMapper<ShopAllotDetail,String> {

    List<ShopAllotDetail> findByShopAllotId(String shopAllotId);

    List<ShopAllotDetail> findByShopAllotIds(List<String> shopAllotIdList);

    int deleteByShopAllotId(String shopAllotId);

    List<ShopAllotDetailDto> getShopAllotDetailListForEdit(@Param("shopAllotId")  String shopAllotId, @Param("fromShopId")String fromShopId, @Param("toShopId")String toShopId);

    List<ShopAllotDetailDto> getShopAllotDetailListForNew(@Param("fromShopId")String fromShopId, @Param("toShopId")String toShopId);
}
