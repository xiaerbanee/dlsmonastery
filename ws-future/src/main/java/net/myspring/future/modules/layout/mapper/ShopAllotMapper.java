package net.myspring.future.modules.layout.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopAllot;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.future.modules.layout.web.query.ShopAllotQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Mapper
public interface ShopAllotMapper extends MyMapper<ShopAllot,String> {


    Page<ShopAllotDto> findPage(Pageable pageable, @Param("p") ShopAllotQuery shopAllotQuery);

    String findMaxBusinessId(LocalDate localDate);

}
