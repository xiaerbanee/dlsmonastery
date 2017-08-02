package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.Express
import net.myspring.future.modules.crm.dto.ExpressDto
import net.myspring.future.modules.crm.web.query.ExpressQuery
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


interface ExpressRepository : BaseRepository<Express, String>,ExpressRepositoryCustom {

    fun findByEnabledIsTrueAndExpressOrderId(expressOrderId: String): MutableList<Express>

    fun deleteByExpressOrderId(expressOrderId: String)

}

interface ExpressRepositoryCustom{
    fun findPage(pageable : Pageable, expressQuery: ExpressQuery): Page<ExpressDto>

    fun findDto(id: String): ExpressDto
}

class ExpressRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ExpressRepositoryCustom {
    override fun findDto(id: String): ExpressDto {

        return namedParameterJdbcTemplate.queryForObject("""
        SELECT
            ord.extend_type expressOrderExtendType,
            ord.extend_business_id expressOrderExtendBusinessId,
            ord.contator expressOrderContator,
            ord.mobile_phone expressOrderMobilePhone,
            ord.address expressOrderAddress,
            ord.from_depot_id expressOrderFromDepotId,
            ord.to_depot_id expressOrderToDepotId,
            ord.express_company_id expressOrderExpressCompanyId,
            ord.total_qty expressOrderTotalQty,
            toDepot.area_id expressOrderToDepotAreaId,
            t1.*
        FROM
           crm_express t1
           LEFT JOIN crm_express_order ord ON t1.express_order_id = ord.id
           LEFT JOIN crm_depot toDepot ON ord.to_depot_id = toDepot.id
        WHERE
            t1.enabled = 1
            AND t1.id = :id
          """, Collections.singletonMap("id", id), BeanPropertyRowMapper(ExpressDto::class.java))

    }

    override fun findPage(pageable : Pageable, expressQuery: ExpressQuery): Page<ExpressDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
            ord.extend_type expressOrderExtendType,
            ord.extend_business_id expressOrderExtendBusinessId,
            ord.contator expressOrderContator,
            ord.mobile_phone expressOrderMobilePhone,
            ord.address expressOrderAddress,
            ord.from_depot_id expressOrderFromDepotId,
            ord.to_depot_id expressOrderToDepotId,
            ord.express_company_id expressOrderExpressCompanyId,
            ord.total_qty expressOrderTotalQty,
            toDepot.area_id expressOrderToDepotAreaId,
            t1.*
        FROM
            crm_express t1
            LEFT JOIN crm_express_order ord ON t1.express_order_id = ord.id
            LEFT JOIN crm_depot toDepot ON ord.to_depot_id = toDepot.id
        WHERE
            t1.enabled = 1
        """)
        if(StringUtils.isNotBlank(expressQuery.code)){
            sb.append("""  and t1.code like concat('%', :code,'%')  """)
        }
        if(expressQuery.createdDateStart != null){
            sb.append("""  and t1.created_date >= :createdDateStart  """)
        }
        if(expressQuery.createdDateEnd != null){
            sb.append("""   and t1.created_date < :createdDateEnd  """)
        }
        if(StringUtils.isNotBlank(expressQuery.expressOrderExtendType)){
            sb.append("""  and ord.extend_type =  :expressOrderExtendType """)
        }
        if(StringUtils.isNotBlank(expressQuery.expressOrderExtendBusinessId)){
            sb.append("""   and ord.extend_business_id like concat('%', :expressOrderExtendBusinessId,'%')  """)
        }
        if(StringUtils.isNotBlank(expressQuery.expressOrderExpressCompanyId)){
            sb.append("""  and ord.express_company_id = :expressOrderExpressCompanyId  """)
        }
        if(StringUtils.isNotBlank(expressQuery.expressOrderToDepotId)){
            sb.append("""  and ord.to_depot_id =  :expressOrderToDepotId  """)
        }
        if(StringUtils.isNotBlank(expressQuery.expressOrderFromDepotId)){
            sb.append("""   and ord.from_depot_id = :expressOrderFromDepotId   """)
        }
        if (CollectionUtil.isNotEmpty(expressQuery.officeIdList)) {
            sb.append("""  and toDepot.office_id in (:officeIdList)  """)
        }
        if (CollectionUtil.isNotEmpty(expressQuery.depotIdList)) {
            sb.append("""  and toDepot.id in (:depotIdList)  """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(expressQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ExpressDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap,Long::class.java)
        return PageImpl(list,pageable,count)
    }


}