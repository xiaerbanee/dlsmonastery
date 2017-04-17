package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopAllot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Map;

@Mapper
public interface ShopAllotMapper extends MyMapper<ShopAllot,String> {

    Page<ShopAllot> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    String findMaxBusinessId(LocalDate localDate);

}
