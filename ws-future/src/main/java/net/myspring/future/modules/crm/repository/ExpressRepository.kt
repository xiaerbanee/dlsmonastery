package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.Express
import net.myspring.future.modules.crm.domain.ExpressOrder

import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import net.myspring.future.modules.crm.dto.ExpressDto
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import org.apache.ibatis.annotations.Param
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query


interface ExpressRepository : BaseRepository<Express, String> {


    fun findByExpressOrderId(expressOrderId: String): List<Express>


    @Query("""
    SELECT
        ord.extend_type expressOrderExtendType,
        ord.extend_business_id expressOrderExtendBusinessId,
        ord.contator expressOrderContator,
        ord.mobile_phone expressOrderMobilePhone,
        ord.address expressOrderAddress,
        ord.from_depot_id expressOrderFromDepotId,
        ord.to_depot_id expressOrderToDepotId,
        ord.express_company_id expressOrderExpressCompanyId,
        t1.*
    FROM
       crm_express t1
       LEFT JOIN crm_express_order ord ON t1.express_order_id = ord.id
    WHERE
        t1.enabled = 1
        AND t1.id = ?1
        """, nativeQuery = true)
    fun findDto(id: String): ExpressDto



}
