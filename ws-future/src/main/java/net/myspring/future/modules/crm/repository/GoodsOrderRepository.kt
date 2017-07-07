package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.api.web.query.B2b2Query
import net.myspring.future.modules.crm.domain.GoodsOrder
import net.myspring.future.modules.crm.dto.GoodsOrderDto
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.IdUtils
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.util.*


interface GoodsOrderRepository : BaseRepository<GoodsOrder, String>, GoodsOrderRepositoryCustom {

    fun findByBusinessIdIn(businessIdList: List<String>):MutableList<GoodsOrder>

    fun findByBusinessId(businessId: String):GoodsOrder

    fun findByIdInAndEnabledIsTrue(ids:MutableList<String>):MutableList<GoodsOrder>

    @Query("""
        update  #{#entityName} t set t.status=?1 where t.id in (?2)
     """)
    @Modifying
    fun updateStatusByIdIn( status:String,ids: MutableList<String>):Int
}

interface GoodsOrderRepositoryCustom {
    fun findAll(pageable: Pageable, goodsOrderQuery: GoodsOrderQuery): Page<GoodsOrderDto>

    fun findNextBusinessId(date: LocalDate): String

    fun findLxMallOrderBybusinessIdList(businessIdList: List<String>): List<String>

    fun findB2bTask(pageable: Pageable,b2b2Query: B2b2Query):Page<GoodsOrder>

    fun findDtoListByIdList(goodsOrderIdList: List<String>): List<GoodsOrderDto>

}

class GoodsOrderRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : GoodsOrderRepositoryCustom {
    override fun findDtoListByIdList(goodsOrderIdList: List<String>): List<GoodsOrderDto> {
        if(CollectionUtil.isEmpty(goodsOrderIdList)){
            return ArrayList()
        }
        return namedParameterJdbcTemplate.query("""
            SELECT
              t2.express_codes as expressOrderExpressCodes,
              shop.client_id clientId,
              shop.office_id shopOfficeId,
              shop.area_id shopAreaId,
              depotShop.area_type shopDepotShopAreaType,
              t1.*
            FROM crm_goods_order t1
                      LEFT JOIN crm_express_order t2 ON t1.express_order_id = t2.id
                      LEFT JOIN crm_depot shop ON t1.shop_id = shop.id
                      LEFT JOIN crm_depot_shop depotShop ON shop.depot_shop_id = depotShop.id
            where  t1.id IN (:goodsOrderIdList)
                """, Collections.singletonMap("goodsOrderIdList", goodsOrderIdList), BeanPropertyRowMapper(GoodsOrderDto::class.java))
    }

    override fun findB2bTask(pageable: Pageable,b2b2Query: B2b2Query): Page<GoodsOrder> {
        val sb = StringBuilder("""
          SELECT
        DISTINCT t2.*
        FROM
        api_carrier_order t1,
        crm_goods_order t2,
        crm_depot t3,
        WHERE
        t2.shop_id = t3.id
        AND t1.goods_order_id = t2.id
        and t1.code like CONCAT('%', 'DD', '%')
        AND t1.enabled = 1
        AND t2.enabled = 1
        """)
        if(StringUtils.isNotBlank(b2b2Query.storeId)||CollectionUtil.isNotEmpty(b2b2Query.proxyAreaIdList)){
            sb.append(" and (")
            if(StringUtils.isNotBlank(b2b2Query.storeId)){
                sb.append("t2.store_id !=:storeId")
            }
            if(CollectionUtil.isNotEmpty(b2b2Query.proxyAreaIdList)){
                sb.append("OR  t3.area_id in (:proxyAreaIdList)")
            }
            sb.append(")");
        }
        if(StringUtils.isNotBlank(b2b2Query.status)){
            sb.append(" and (t1.status =:status or t1.status is null)");
        }
        if(b2b2Query.shipDateStart!=null){
            sb.append(" AND t2.ship_date >=:shipDateStart");
        }
        if(b2b2Query.shipDateEnd!=null){
            sb.append(" AND t2.ship_date <=:shipDateEnd");
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(b2b2Query), BeanPropertyRowMapper(GoodsOrder::class.java))
        return PageImpl(list,pageable,((pageable.pageNumber + 100) * pageable.pageSize).toLong())
    }

    override fun findLxMallOrderBybusinessIdList(businessIdList: List<String>): List<String> {
        if(CollectionUtil.isEmpty(businessIdList)){
            return ArrayList()
        }
        return namedParameterJdbcTemplate.queryForList("""
            select t.business_id
            from crm_goods_order t
            where t.business_id in (:businessIdList)
                      and t.lx_mall_order=1
                      and t.enabled = 1
                """, Collections.singletonMap("businessIdList", businessIdList), String::class.java)
    }

    override fun findNextBusinessId(date: LocalDate): String {
        val sql = "select max(t.business_id) from crm_goods_order t where t.bill_date = :date"
        val maxBusinessId = namedParameterJdbcTemplate.queryForObject(sql,Collections.singletonMap("date", date),String::class.java)
        return IdUtils.getNextBusinessId(maxBusinessId, date)
    }

    override fun findAll(pageable: Pageable, goodsOrderQuery: GoodsOrderQuery): Page<GoodsOrderDto> {
        val sb = StringBuilder("""
            SELECT
              t2.express_codes as expressOrderExpressCodes,
              shop.client_id clientId,
              shop.office_id shopOfficeId,
              shop.area_id shopAreaId,
              depotShop.area_type shopDepotShopAreaType,
              t1.*
            FROM crm_goods_order t1
                      LEFT JOIN crm_express_order t2 ON t1.express_order_id = t2.id
                      LEFT JOIN crm_depot shop ON t1.shop_id = shop.id
                      LEFT JOIN crm_depot_shop depotShop ON shop.depot_shop_id = depotShop.id
            where  t1.enabled = 1
        """)
        if (CollectionUtil.isNotEmpty(goodsOrderQuery.statusList)) {
            sb.append(" and t1.status in (:statusList)")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.status)) {
            sb.append(" and t1.status = :status")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.netType)) {
            sb.append(" and t1.net_type = :netType")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.businessId)) {
            sb.append(" and t1.business_id like concat('%',:businessId,'%')")
        }
        if (goodsOrderQuery.billDateStart != null) {
            sb.append(" and t1.bill_date > :billDateStart")
        }
        if (goodsOrderQuery.billDateEnd != null) {
            sb.append(" and t1.bill_date < :billDateEnd ")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.shipType)) {
            sb.append(" and t1.ship_type = :shipType ")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.areaId)) {
            sb.append(" and shop.area_id = :areaId ")
        }
        if (goodsOrderQuery.shipDateStart != null) {
            sb.append(" and t1.ship_date > :shipDateStart")
        }
        if (goodsOrderQuery.shipDateEnd != null) {
            sb.append(" and t1.ship_date < :shipDateEnd")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.shopId)) {
            sb.append(" and t1.shop_id = :shopId ")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.shopName)) {
            sb.append(" and shop.name like concat('%',:shopName,'%')  ")
        }
        if (CollectionUtil.isNotEmpty(goodsOrderQuery.storeIdList)) {
            sb.append(" and t1.store_id IN (:storeIdList) ")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.createdBy)) {
            sb.append(" and t1.created_by = :createdBy ")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.outCode)) {
            sb.append(" and t1.out_code like concat('%',:outCode,'%')")
        }
        if (goodsOrderQuery.createdDateStart != null) {
            sb.append(" and t1.created_date > :createdDateStart")
        }
        if (goodsOrderQuery.createdDateEnd != null) {
            sb.append(" and t1.created_date <:createdDateEnd")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.expressCodes)) {
            sb.append("""   and t1.express_order_id in (
            select express.express_order_id from crm_express express where express.code in (:expresscodeList)
            )
         """)
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.businessIds)) {
            sb.append(" and t1.business_id in (:businessIdList)")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.remarks)) {
            sb.append(" and t1.remarks like concat('%',:remarks,'%')")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.expressCode)) {
            sb.append(" and t1.express_order_id in(select express.express_order_id from crm_express express where express.code like concat('%',:expressCode,'%'))")
        }
        if (goodsOrderQuery.lxMallOrder != null && goodsOrderQuery.lxMallOrder) {
            sb.append(" and t1.lx_mall_order = 1  ")
        }
        if (goodsOrderQuery.lxMallOrder != null && !goodsOrderQuery.lxMallOrder) {
            sb.append(" and t1.lx_mall_order = 0  ")
        }
        if (CollectionUtil.isNotEmpty(goodsOrderQuery.officeIdList)) {
            sb.append(" and shop.office_id in (:officeIdList)")
        }
        if (CollectionUtil.isNotEmpty(goodsOrderQuery.depotIdList)) {
            sb.append(" and t1.shop_id in (:depotIdList)")
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(goodsOrderQuery), BeanPropertyRowMapper(GoodsOrderDto::class.java))
        return PageImpl(list,pageable,((pageable.pageNumber + 100) * pageable.pageSize).toLong())
    }
}
