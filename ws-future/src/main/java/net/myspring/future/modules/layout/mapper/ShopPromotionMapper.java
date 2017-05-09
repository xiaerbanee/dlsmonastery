package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopPromotion;
import net.myspring.future.modules.layout.dto.ShopPromotionDto;
import net.myspring.future.modules.layout.web.query.ShopPromotionQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Map;

@Mapper
public interface ShopPromotionMapper extends MyMapper<ShopPromotion, String> {

    Page<ShopPromotionDto> findPage(Pageable pageable, @Param("p")ShopPromotionQuery shopPromotionQuery);

    String findMaxBusinessId(@Param("localDate") LocalDate localDate);
}
