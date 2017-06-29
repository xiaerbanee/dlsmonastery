package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.GoodsOrder
import net.myspring.future.modules.crm.dto.GoodsOrderDto
import net.myspring.future.modules.crm.dto.ImeAllotDto
import net.myspring.future.modules.crm.web.form.GoodsOrderBillDetailForm
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import net.myspring.util.time.LocalDateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.util.*


interface GoodsOrderRepository : BaseRepository<GoodsOrder, String>, GoodsOrderRepositoryCustom {
}

interface GoodsOrderRepositoryCustom {
    fun findAll(pageable: Pageable, goodsOrderQuery: GoodsOrderQuery): Page<GoodsOrderDto>?

    fun findNextBusinessId(companyId:String,date: LocalDate): String

    fun findLxMallOrderBybusinessIdList(businessIdList: List<String>): List<String>
}

class GoodsOrderRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : GoodsOrderRepositoryCustom {
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

    override fun findNextBusinessId(companyId:String,date: LocalDate): String {
        var sql = "select max(t.business_id) from crm_goods_order t where t.bill_date = :date";
        var maxBusinessId = namedParameterJdbcTemplate.queryForObject(sql,Collections.singletonMap("date", date),Long::class.java);
        if (maxBusinessId == null) {
            maxBusinessId =(companyId + LocalDateUtils.format(date,"yyyyMMdd") + "00000").toLong();
        }
        return (maxBusinessId + 1).toString();
    }

    override fun findAll(pageable: Pageable, goodsOrderQuery: GoodsOrderQuery): Page<GoodsOrderDto>? {
        var sb = StringBuilder("""
            SELECT
              t2.express_codes as expressOrderExpressCodes,
              t1.*
            FROM crm_goods_order t1
                      LEFT JOIN crm_express_order t2 ON t1.express_order_id = t2.id
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
            sb.append(" and t1.shop_id in(select id from crm_depot where area_id = :areaId) ")
        }
        if (goodsOrderQuery.shipDateStart != null) {
            sb.append(" and t1.ship_date > :shipDateStart")
        }
        if (goodsOrderQuery.shipDateEnd != null) {
            sb.append(" and t1.ship_date < :shipDateEnd")
        }
        if (StringUtils.isNoneBlank(goodsOrderQuery.shopName)) {
            sb.append(" and t1.shop_id in(select id from crm_depot where name like concat('%',:shopName,'%') )");
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.storeId)) {
            sb.append(" and t1.store_id = :storeId ")
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
        if (StringUtils.isNoneBlank(goodsOrderQuery.businessIds)) {
            sb.append(" and t1.business_id in (:businessIdList)")
        }
        if (StringUtils.isNoneBlank(goodsOrderQuery.remarks)) {
            sb.append(" and t1.remarks like concat('%',:remarks,'%')")
        }
        if (StringUtils.isNoneBlank(goodsOrderQuery.expressCode)) {
            sb.append(" and t1.express_order_id in(select express.express_order_id from crm_express express where express.code like concat('%',:expressCode,'%'))")
        }
        if (goodsOrderQuery.lxMallOrder != null && goodsOrderQuery.lxMallOrder) {
            sb.append(" and t1.lx_mall_order = 1  ")
        }
        if (goodsOrderQuery.lxMallOrder != null && !goodsOrderQuery.lxMallOrder) {
            sb.append(" and t1.lx_mall_order = 0  ")
        }
        if (CollectionUtil.isNotEmpty(goodsOrderQuery.officeIdList)) {
            sb.append(" and t1.shop_id in (select shop.id from crm_depot shop where shop.office_id in (:officeIdList))")
        }
        if (CollectionUtil.isNotEmpty(goodsOrderQuery.depotIdList)) {
            sb.append(" and t1.shop_id in (:depotIdList)")
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(goodsOrderQuery), BeanPropertyRowMapper(GoodsOrderDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(goodsOrderQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}