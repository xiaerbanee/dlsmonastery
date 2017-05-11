package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDto;
import net.myspring.future.modules.layout.web.query.AdGoodsOrderQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Map;

@Mapper
public interface AdGoodsOrderMapper extends MyMapper<AdGoodsOrder,String> {

    Page<AdGoodsOrderDto> findPage(@Param("pageable") Pageable pageable, @Param("p")AdGoodsOrderQuery adGoodsOrderQuery);

    String findMaxBusinessId(@Param("localDate") LocalDate localDate);

}
