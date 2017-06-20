package net.myspring.future.modules.layout.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.dto.StoreAllotImeDto
import net.myspring.future.modules.layout.domain.AdGoodsOrder
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailExportDto
import net.myspring.future.modules.layout.dto.AdGoodsOrderDto
import net.myspring.future.modules.layout.web.query.AdGoodsOrderQuery
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
import java.time.LocalDateTime
import java.util.*

interface AdGoodsOrderRepository : BaseRepository<AdGoodsOrder,String>,AdGoodsOrderRepositoryCustom{
    @Query("""
    SELECT
        MAX(t1.businessId)
    FROM
        #{#entityName} t1
    WHERE
        t1.billDate = ?1
    """)
    fun findMaxBusinessId(billDate: LocalDate): String
}

interface AdGoodsOrderRepositoryCustom{
    fun findPage(pageable: Pageable,adGoodsOrderQuery: AdGoodsOrderQuery): Page<AdGoodsOrderDto>

    fun findDto(id: String): AdGoodsOrderDto

}

class AdGoodsOrderRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AdGoodsOrderRepositoryCustom{

    override fun findDto(id: String): AdGoodsOrderDto {
        return namedParameterJdbcTemplate.queryForObject("""
            SELECT
                shop.office_id shopOfficeId,
                shop.area_id shopAreaId,
                expressOrder.express_codes expressOrderExpressCodes,
                expressOrder.mobile_phone expressOrderMobilePhone,
                expressOrder.contator expressOrderContator,
                expressOrder.address expressOrderAddress,
                expressOrder.express_company_id expressOrderExpressCompanyId,
                expressOrder.out_print_date expressOrderOutPrintDate,
                expressOrder.should_get expressOrderShouldGet,
                expressOrder.should_pay expressOrderShouldPay,
                expressOrder.real_pay expressOrderRealPay,
                depotShop.area_type depotShopAreaType,
                t1.*
            FROM
                crm_ad_goods_order t1
                LEFT JOIN crm_depot shop ON t1.shop_id = shop.id
                LEFT JOIN crm_depot_shop depotShop ON shop.depot_shop_id = depotShop.id
                LEFT JOIN crm_express_order expressOrder ON t1.express_order_id = expressOrder.id
            WHERE
                t1.id = :id

          """, Collections.singletonMap("id", id), BeanPropertyRowMapper(AdGoodsOrderDto::class.java))
    }

    override fun findPage(pageable: Pageable,adGoodsOrderQuery: AdGoodsOrderQuery): Page<AdGoodsOrderDto>{

        val sb = StringBuffer()
        sb.append("""
            SELECT
                shop.office_id shopOfficeId,
                shop.area_id shopAreaId,
                expressOrder.express_codes expressOrderExpressCodes,
                expressOrder.mobile_phone expressOrderMobilePhone,
                expressOrder.contator expressOrderContator,
                expressOrder.address expressOrderAddress,
                expressOrder.express_company_id expressOrderExpressCompanyId,
                expressOrder.out_print_date expressOrderOutPrintDate,
                expressOrder.should_get expressOrderShouldGet,
                expressOrder.should_pay expressOrderShouldPay,
                expressOrder.real_pay expressOrderRealPay,
                depotShop.area_type depotShopAreaType,
                t1.*
            FROM
                crm_ad_goods_order t1
                LEFT JOIN crm_depot shop ON t1.shop_id = shop.id
                LEFT JOIN crm_depot_shop depotShop ON shop.depot_shop_id = depotShop.id
                LEFT JOIN crm_express_order expressOrder ON t1.express_order_id = expressOrder.id
            WHERE
                t1.enabled = 1
                AND t1.company_id = :companyId

        """)
        if (CollectionUtil.isNotEmpty(adGoodsOrderQuery.idList)) {
            sb.append("""  and t1.id IN (:idList) """)
        }
        if (adGoodsOrderQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (adGoodsOrderQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateStart """)
        }
        if (StringUtils.isNotBlank(adGoodsOrderQuery.billType)) {
            sb.append("""  and t1.bill_type = :billType """)
        }
        if (StringUtils.isNotBlank(adGoodsOrderQuery.remarks)) {
            sb.append("""  and t1.remarks LIKE CONCAT('%', :remarks,'%')   """)
        }
        if (StringUtils.isNotBlank(adGoodsOrderQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if (StringUtils.isNotBlank(adGoodsOrderQuery.parentId)) {
            sb.append("""  and t1.parent_id = :parentId """)
        }
        if (StringUtils.isNotBlank(adGoodsOrderQuery.shopAreaId)) {
            sb.append("""  and shop.area_id = :shopAreaId """)
        }
        if (StringUtils.isNotBlank(adGoodsOrderQuery.storeId)) {
            sb.append("""  and t1.store_id = :storeId """)
        }
        if (StringUtils.isNotBlank(adGoodsOrderQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (adGoodsOrderQuery.billDateStart != null) {
            sb.append("""  and t1.bill_date  >= :billDateStart """)
        }
        if (adGoodsOrderQuery.billDateEnd != null) {
            sb.append("""  and t1.bill_date < :billDateEnd """)
        }
        if (StringUtils.isNotBlank(adGoodsOrderQuery.processStatus)) {
            sb.append("""  and t1.process_status = :processStatus """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(adGoodsOrderQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(AdGoodsOrderDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)

    }
}