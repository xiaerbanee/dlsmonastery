package net.myspring.future.modules.crm.repository

import com.google.common.collect.Lists
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.GoodsOrderIme
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto
import net.myspring.future.modules.crm.web.query.GoodsOrderImeQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface GoodsOrderImeRepository : BaseRepository<GoodsOrderIme, String>, GoodsOrderImeRepositoryCustom {

    fun findByEnabledIsTrueAndGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderIme>

    fun findByGoodsOrderIdIn(goodsOrderIdList: MutableList<String>): MutableList<GoodsOrderIme>

}


interface GoodsOrderImeRepositoryCustom{

    fun findDtoListByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderImeDto>

    fun findDtoListByGoodsOrderIdList(goodsOrderIdList: List<String>): MutableList<GoodsOrderImeDto>

    fun findPage(pageable: Pageable, goodsOrderImeQuery: GoodsOrderImeQuery): Page<GoodsOrderImeDto>

}

class GoodsOrderImeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): GoodsOrderImeRepositoryCustom{
    override fun findPage(pageable: Pageable, goodsOrderImeQuery: GoodsOrderImeQuery): Page<GoodsOrderImeDto> {

        val sb = StringBuilder("""
            SELECT
              t2.status goodsOrderStatus,
              t2.business_id goodsOrderBusinessId,
              t2.created_date goodsOrderCreatedDate,
              ime.ime productImeIme,
              ime.ime2 productImeIme2,
              ime.meid productImeMeid,
              product.name productName,
              shop.name goodsOrderShopName,
              store.name goodsOrderStoreName,
              t1.*
            FROM crm_goods_order_ime t1
                      LEFT JOIN crm_goods_order t2 ON t1.goods_order_id = t2.id
                      LEFT JOIN crm_product_ime ime ON t1.product_ime_id = ime.id
                      LEFT JOIN crm_product product ON t1.product_id = product.id
                      LEFT JOIN crm_depot shop ON t2.shop_id = shop.id
                      LEFT JOIN crm_depot store ON t2.store_id = store.id
            where  t1.enabled = 1
        """)
        if (goodsOrderImeQuery.createdDateStart != null) {
            sb.append(" and t2.created_date > :createdDateStart")
        }
        if (goodsOrderImeQuery.createdDateEnd != null) {
            sb.append(" and t2.created_date <:createdDateEnd")
        }
        if (goodsOrderImeQuery.billDateStart != null) {
            sb.append(" and t2.bill_date > :billDateStart")
        }
        if (goodsOrderImeQuery.billDateEnd != null) {
            sb.append(" and t2.bill_date < :billDateEnd ")
        }
        if (goodsOrderImeQuery.shipDateStart != null) {
            sb.append(" and t2.ship_date > :shipDateStart")
        }
        if (goodsOrderImeQuery.shipDateEnd != null) {
            sb.append(" and t2.ship_date < :shipDateEnd")
        }
        if (StringUtils.isNotBlank(goodsOrderImeQuery.status)) {
            sb.append(" and t2.status = :status")
        }
        if (StringUtils.isNotBlank(goodsOrderImeQuery.remarks)) {
            sb.append(" and t2.remarks like concat('%',:remarks,'%')")
        }
        if (CollectionUtil.isNotEmpty(goodsOrderImeQuery.imeList)) {
            sb.append(" and ime.ime = (:imeList)")
        }
        if (StringUtils.isNotBlank(goodsOrderImeQuery.createdBy)) {
            sb.append(" and t2.created_by = :createdBy ")
        }
        if (StringUtils.isNotBlank(goodsOrderImeQuery.shopName)) {
            sb.append(" and shop.name like concat('%',:shopName,'%')  ")
        }
        if (StringUtils.isNotBlank(goodsOrderImeQuery.storeName)) {
            sb.append(" and store.name like concat('%',:storeName,'%')  ")
        }
        if (StringUtils.isNotBlank(goodsOrderImeQuery.productName)) {
            sb.append(" and product.name like concat('%',:productName,'%')  ")
        }
        if (StringUtils.isNotBlank(goodsOrderImeQuery.businessId)) {
            sb.append(" and t2.business_id = :businessId ")
        }
        if (goodsOrderImeQuery.lxMallOrder != null && goodsOrderImeQuery.lxMallOrder) {
            sb.append(" and t2.lx_mall_order = 1  ")
        }
        if (goodsOrderImeQuery.lxMallOrder != null && !goodsOrderImeQuery.lxMallOrder) {
            sb.append(" and t2.lx_mall_order = 0  ")
        }
        if (CollectionUtil.isNotEmpty(goodsOrderImeQuery.officeIdList)) {
            sb.append(" and shop.office_id in (:officeIdList) ")
        }
        if (CollectionUtil.isNotEmpty(goodsOrderImeQuery.depotIdList)) {
            sb.append(" and t2.shop_id in (:depotIdList) ")
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(goodsOrderImeQuery), BeanPropertyRowMapper(GoodsOrderImeDto::class.java))
        return PageImpl(list,pageable,((pageable.pageNumber + 100) * pageable.pageSize).toLong())

    }

    override fun findDtoListByGoodsOrderIdList(goodsOrderIdList: List<String>): MutableList<GoodsOrderImeDto> {
        if(CollectionUtil.isEmpty(goodsOrderIdList)){
            return ArrayList()
        }
        return namedParameterJdbcTemplate.query("""
        SELECT
            t2.ime productImeIme,
            t2.ime2 productImeIme2,
            t2.meid productImeMeid,
            t3.name productName,
            t1.*
        FROM
            crm_goods_order_ime t1
            LEFT JOIN crm_product_ime t2 ON t1.product_ime_id = t2.id
            LEFT JOIN crm_product t3 ON t1.product_id = t3.id
        WHERE
            t1.goods_order_id IN (:goodsOrderIdList)
            AND t1.enabled = 1

          """, Collections.singletonMap("goodsOrderIdList", goodsOrderIdList), BeanPropertyRowMapper(GoodsOrderImeDto::class.java))
    }

    override fun findDtoListByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderImeDto> {
        return findDtoListByGoodsOrderIdList(Lists.newArrayList(goodsOrderId))
    }


}