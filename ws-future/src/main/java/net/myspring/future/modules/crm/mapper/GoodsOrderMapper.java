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

    List<GoodsOrder> findList(GoodsOrderQuery goodsOrderQuery);

    GoodsOrder findByBusinessId(String businessId);

    List<GoodsOrder> findByStoreBillData(@Param("billDate") LocalDate billDate, @Param("storeId") String storeId, @Param("status") String status);

    String findMaxBusinessId(@Param("localDate") LocalDate localDate);

    List<GoodsOrder> findByShopIdAndStatus(@Param("shopId") String shopId, @Param("status") String status);

    GoodsOrderDto findDto(@Param("goodsOrderId")  String goodsOrderId);
}
