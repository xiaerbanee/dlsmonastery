package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.web.query.GoodsOrderDetailQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface GoodsOrderDetailMapper extends MyMapper<GoodsOrderDetail,String> {

    List<GoodsOrderDetail> findByGoodsOrderId(String goodsOrderId);

    List<GoodsOrderDetail> findByGoodsOrderIds(List<String> goodsOrderIdList);

    List<GoodsOrderDetail> findByParentOfficeIdAndCreatedDate(@Param("areaId") String areaId, @Param("dateStart") LocalDateTime dateStart, @Param("dateEnd") LocalDateTime dateEnd);

    List<GoodsOrderDetail> findByParentOfficeIdAndBillDate(@Param("areaId") String areaId, @Param("dateStart") LocalDateTime dateStart, @Param("dateEnd") LocalDateTime dateEnd, @Param("shipTypes") List<String> shipTypes);

    List<GoodsOrderDetailDto> getDtoListForNewWithoutAreaQty(@Param("depotId")  String depotId, @Param("netType")  String netType,  @Param("showAll") Boolean showAll);

    GoodsOrderDetailDto getDtoWithAreaQty(@Param("productId")  String productId, @Param("depotId")   String depotId, @Param("dateStart") LocalDateTime dateStart, @Param("dateEnd") LocalDateTime dateEnd);

    List<GoodsOrderDetailDto> findListForNewOrEditOrBillWithAreaQty(@Param("p") GoodsOrderDetailQuery goodsOrderDetailQuery);

    List<GoodsOrderDetailDto> findDtoListByGoodsOrderId(@Param("goodsOrderId") String goodsOrderId);
}
