package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository

import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime


interface GoodsOrderDetailRepository : BaseRepository<GoodsOrderDetail, String> {

    fun findByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderDetail>

    fun findByGoodsOrderIdIn(goodsOrderIds:MutableList<String>):MutableList<GoodsOrderDetail>

    @Query("""
        select
            t1
        from
            GoodsOrderDetail t1,
            GoodsOrder t2,
            Depot t3
        where
            t1.goodsOrderId=t2.id
        and t2.shopId = t3.id
        and t3.areaId = ?1
        and t2.createdDate >=?2
        and t2.createdDate < ?3
        and t2.enabled=1
    """)
    fun findByAreaIdAndCreatedDate(areaId:String, createdDateStart: LocalDateTime, createdDateEnd: LocalDateTime):List<GoodsOrderDetail>
}
