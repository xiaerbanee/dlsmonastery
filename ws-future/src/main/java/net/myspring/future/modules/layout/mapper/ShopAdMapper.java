package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopAd;
import net.myspring.future.modules.layout.dto.ShopAdDto;
import net.myspring.future.modules.layout.web.query.ShopAdQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShopAdMapper extends MyMapper<ShopAd,String> {

    Page<ShopAdDto> findPage(Pageable pageable, @Param("p")ShopAdQuery shopAdQuery);

    List<ShopAd> findByFilter(@Param("p") Map<String, Object> map);
}
