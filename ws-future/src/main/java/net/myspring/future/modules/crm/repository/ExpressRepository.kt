package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.Express
import net.myspring.future.modules.crm.domain.ExpressOrder

import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import net.myspring.future.modules.crm.dto.ExpressDto
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery
import net.myspring.future.modules.crm.web.query.ExpressQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.data.repository.query.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import javax.persistence.EntityManager


interface ExpressRepository : BaseRepository<Express, String>,ExpressRepositoryCustom {

    fun findByExpressOrderId(expressOrderId: String): MutableList<Express>

}

interface ExpressRepositoryCustom{
    fun findPage(pageable : Pageable, expressrQuery : ExpressQuery): Page<ExpressDto>

    fun findDto(id: String): ExpressDto
}

class ExpressRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ExpressRepositoryCustom {
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
            t1.*
        FROM
           crm_express t1
           LEFT JOIN crm_express_order ord ON t1.express_order_id = ord.id
        WHERE
            t1.enabled = 1
            AND t1.id = :id
          """, Collections.singletonMap("id", id), MyBeanPropertyRowMapper(ExpressDto::class.java))
    }

    override fun findPage(pageable : Pageable, expressrQuery: ExpressQuery): Page<ExpressDto> {
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
            t1.*
        FROM
            crm_express t1
            LEFT JOIN crm_express_order ord ON t1.express_order_id = ord.id
        WHERE
            t1.enabled = 1
        """)
        if(StringUtils.isNotBlank(expressrQuery.code)){
            sb.append("""  and t1.code like concat('%', :code,'%')  """)
        }
        if(expressrQuery.createdDateStart != null){
            sb.append("""  and t1.created_date >= :createdDateStart  """)
        }
        if(expressrQuery.createdDateEnd != null){
            sb.append("""   and t1.created_date < :createdDateEnd  """)
        }
        if(StringUtils.isNotBlank(expressrQuery.expressOrderExtendType)){
            sb.append("""  and ord.extend_type =  :expressOrderExtendType """)
        }

        if(StringUtils.isNotBlank(expressrQuery.expressOrderExtendBusinessId)){
            sb.append("""   and ord.extend_business_id like concat('%', :expressOrderExtendBusinessId,'%')  """)
        }

        if(StringUtils.isNotBlank(expressrQuery.expressOrderExpressCompanyId)){
            sb.append("""  and ord.express_company_id = :expressOrderExpressCompanyId  """)
        }
        if(StringUtils.isNotBlank(expressrQuery.expressOrderToDepotId)){
            sb.append("""  and ord.to_depot_id =  :expressOrderToDepotId  """)
        }
        if(StringUtils.isNotBlank(expressrQuery.expressOrderFromDepotId)){
            sb.append("""   and ord.from_depot_id = :expressOrderFromDepotId   """)
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(expressrQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ExpressDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)


    }


}