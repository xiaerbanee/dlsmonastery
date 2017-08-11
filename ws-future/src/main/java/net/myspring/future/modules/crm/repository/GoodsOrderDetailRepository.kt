package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto
import net.myspring.future.modules.crm.web.query.GoodsOrderDetailQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
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

    fun findPage(pageable: Pageable,goodsOrderDetailQuery: GoodsOrderDetailQuery):Page<GoodsOrderDetailDto>

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

    override fun findPage(pageable: Pageable,goodsOrderDetailQuery: GoodsOrderDetailQuery):Page<GoodsOrderDetailDto>{
        val sb = StringBuilder("""
            SELECT
                t2.business_id bussinessId,
                t2.store_id storeId,
                t2.bill_date billDate,
                t2.created_by createdBy,
                t2.created_date createdDate,
                t2.remarks remarks,
                t2.status status,
                product.name productName,
                depot.id depotId,
                depot.name depotName,
                depot.area_id areaId,
	            depot.office_id officeId,
                t1.*
            FROM
                crm_goods_order_detail t1
                LEFT JOIN crm_goods_order t2 ON t1.goods_order_id = t2.id
                LEFT JOIN crm_product product ON product.id = t1.product_id
                LEFT JOIN crm_depot depot ON t2.shop_id = depot.id
            WHERE
                t2.enabled = 1
        """)
        if (StringUtils.isNotBlank(goodsOrderDetailQuery.productName)) {
            sb.append(" and product.name like concat('%',:productName,'%')  ")
        }
        if (StringUtils.isNotBlank(goodsOrderDetailQuery.depotName)) {
            sb.append(" and depot.name like concat('%',:depotName,'%')  ")
        }
        if (goodsOrderDetailQuery.createdDateStart != null) {
            sb.append(" and t2.created_date >= :createdDateStart")
        }
        if (goodsOrderDetailQuery.createdDateEnd != null) {
            sb.append(" and t2.created_date < :createdDateEnd")
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(goodsOrderDetailQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(GoodsOrderDetailDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)
    }
}