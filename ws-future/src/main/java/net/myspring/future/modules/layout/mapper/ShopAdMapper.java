package net.myspring.future.modules.layout.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopAd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShopAdMapper extends MyMapper<ShopAd,String> {

    Page<ShopAd> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<ShopAd> findByFilter(@Param("p") Map<String, Object> map);
}
