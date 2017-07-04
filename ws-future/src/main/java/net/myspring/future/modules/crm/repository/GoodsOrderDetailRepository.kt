package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto
import net.myspring.util.collection.CollectionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDateTime
import java.util.*


interface GoodsOrderDetailRepository : BaseRepository<GoodsOrderDetail, String> , GoodsOrderDetailRepositoryCustom {

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


interface GoodsOrderDetailRepositoryCustom{

    fun findDtoListByGoodsOrderIdList(goodsOrderIdList: List<String>): MutableList<GoodsOrderDetailDto>

}

class GoodsOrderDetailRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): GoodsOrderDetailRepositoryCustom{

    override fun findDtoListByGoodsOrderIdList(goodsOrderIdList: List<String>): MutableList<GoodsOrderDetailDto> {
        if(CollectionUtil.isEmpty(goodsOrderIdList)){
            return ArrayList()
        }
        return namedParameterJdbcTemplate.query("""
        SELECT
            t2.name productName,
            t1.*
        FROM
            crm_goods_order_detail t1
            LEFT JOIN crm_product t2 ON t1.product_id = t2.id
        WHERE
            t1.goods_order_id IN (:goodsOrderIdList)

          """, Collections.singletonMap("goodsOrderIdList", goodsOrderIdList), BeanPropertyRowMapper(GoodsOrderDetailDto::class.java))
    }
}