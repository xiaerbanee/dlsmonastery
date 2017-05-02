package net.myspring.future.modules.crm.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsOrderMapper extends MyMapper<GoodsOrder,String> {

    Page<GoodsOrder> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    GoodsOrder findByBusinessId(String businessId);

    List<GoodsOrder> findByStoreBillData(@Param("billDate") LocalDate billDate, @Param("storeId") String storeId, @Param("status") String status);

    String findMaxBusinessId(@Param("localDate") LocalDate localDate);

    List<GoodsOrder> findByShopIdAndStatus(@Param("shopId") String shopId, @Param("status") String status);
}
