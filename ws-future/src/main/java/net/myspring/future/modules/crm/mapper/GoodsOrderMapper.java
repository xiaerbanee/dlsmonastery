package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface GoodsOrderMapper extends MyMapper<GoodsOrder,String> {

    Page<GoodsOrderDto> findPage(Pageable pageable, @Param("p") GoodsOrderQuery goodsOrderQuery);

    List<GoodsOrder> findList(@Param("p") GoodsOrderQuery goodsOrderQuery);

    String findMaxBusinessId(LocalDate date);
}
