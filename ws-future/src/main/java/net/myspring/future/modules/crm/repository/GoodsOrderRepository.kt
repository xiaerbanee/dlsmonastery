package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.GoodsOrder
import net.myspring.future.modules.crm.dto.GoodsOrderDto
import net.myspring.future.modules.crm.web.form.GoodsOrderBillDetailForm
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery
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
import java.time.LocalDate
import java.util.*


interface GoodsOrderRepository : BaseRepository<GoodsOrder, String>, GoodsOrderRepositoryCustom {

    @Query("""
        SELECT
            MAX(t1.businessId)
        FROM
            #{#entityName} t1
        WHERE
            t1.billDate>=?1
        """)
    fun findMaxBusinessId(date: LocalDate): String
}

interface GoodsOrderRepositoryCustom {
    fun findAll(pageable: Pageable, goodsOrderQuery: GoodsOrderQuery): Page<GoodsOrderDto>?
}

class GoodsOrderRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : GoodsOrderRepositoryCustom {
    override fun findAll(pageable: Pageable, goodsOrderQuery: GoodsOrderQuery): Page<GoodsOrderDto>? {
        var sb = StringBuilder("select * from crm_goods_order where 1=1")
        if (CollectionUtil.isNotEmpty(goodsOrderQuery.statusList)) {
            sb.append(" and status in (:statusList)")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.status)) {
            sb.append(" and status = :status")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.netType)) {
            sb.append(" and net_type = :netType")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.businessId)) {
            sb.append(" and business_id like concat('%',:businessId,'%')")
        }
        if (goodsOrderQuery.billDateStart != null) {
            sb.append(" and bill_data > :billDateStart")
        }
        if (goodsOrderQuery.billDateEnd != null) {
            sb.append(" and bill_data < :billDateEnd ")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.shipType)) {
            sb.append(" and bill_type = :billType ")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.areaId)) {
            //todo
        }
        if (goodsOrderQuery.shipDateStart != null) {
            sb.append(" and ship_date > :shipDateStart")
        }
        if (goodsOrderQuery.shipDateEnd != null) {
            sb.append(" and ship_date < :shipDateEnd")
        }
        if (StringUtils.isNoneBlank(goodsOrderQuery.shopName)) {
            sb.append(" and shop_name like concat('%', :shopName,'%')");
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.storeId)) {
            //todo
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.createdBy)) {
            //todo
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.outCode)) {
            sb.append(" and out_code like concat('%',:outCode,'%')")
        }
        if (goodsOrderQuery.createdDateStart != null) {
            sb.append(" and create_date > :createdDateStart")
        }
        if (goodsOrderQuery.createdDateEnd != null) {
            sb.append(" and create_date <:createDateEnd")
        }
        if (StringUtils.isNotBlank(goodsOrderQuery.expressCodes)) {
            //todo
        }
        if (StringUtils.isNoneBlank(goodsOrderQuery.businessIds)) {
            //todo
        }
        if (StringUtils.isNoneBlank(goodsOrderQuery.remarks)) {
            sb.append(" and remarks like concat('%',:remarks,'%')")
        }
        if (StringUtils.isNoneBlank(goodsOrderQuery.expressCode)) {
            sb.append(" and express_code like concat('%',:expressCode,'%')")
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(goodsOrderQuery), BeanPropertyRowMapper(GoodsOrderDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(goodsOrderQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}