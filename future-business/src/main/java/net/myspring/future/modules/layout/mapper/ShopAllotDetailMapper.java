package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopAllotDetailMapper extends MyMapper<ShopAllotDetail,String> {

    List<ShopAllotDetail> findByShopAllotId(String shopAllotId);

    List<ShopAllotDetail> findByShopAllotIds(List<String> shopAllotIdList);

    int deleteByShopAllotId(String shopAllotId);
}
