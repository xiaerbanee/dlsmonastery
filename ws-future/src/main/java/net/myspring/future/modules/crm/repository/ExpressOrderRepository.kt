package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ExpressOrder
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery
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


interface ExpressOrderRepository : BaseRepository<ExpressOrder, String>, ExpressOrderRepositoryCustom {

    fun findByExtendIdAndExtendType(extendId: String, extendType: String): ExpressOrder

}

interface ExpressOrderRepositoryCustom{
    fun findPage(pageable : Pageable, expressOrderQuery : ExpressOrderQuery): Page<ExpressOrderDto>

    fun findDto(id: String): ExpressOrderDto
}

class ExpressOrderRepositoryImpl @Autowired constructor( val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ExpressOrderRepositoryCustom {
    override fun findDto(id: String): ExpressOrderDto {

        return namedParameterJdbcTemplate.queryForObject("""
       SELECT
            fromDepot.name fromDepotName,
            toDepot.name toDepotName,
            toDepot.district_id toDepotDistrictId,
            expressCompany.name expressCompanyName,
            t1.*
        FROM
            crm_express_order t1
            LEFT JOIN crm_depot fromDepot ON t1.from_depot_id = fromDepot.id
            LEFT JOIN crm_depot toDepot ON t1.to_depot_id = toDepot.id
            LEFT JOIN crm_express_company expressCompany ON t1.express_company_id = expressCompany.id
        WHERE
            t1.id = :id
          """, Collections.singletonMap("id", id), MyBeanPropertyRowMapper(ExpressOrderDto::class.java))
    }

    override fun findPage(pageable : Pageable,expressOrderQuery: ExpressOrderQuery): Page<ExpressOrderDto> {
        val sb = StringBuilder()
        sb.append("""
        SELECT
            fromDepot.name fromDepotName,
            toDepot.name toDepotName,
            toDepot.district_id toDepotDistrictId,
            expressCompany.name expressCompanyName,
            t1.*
        FROM
            crm_express_order t1
            LEFT JOIN crm_depot fromDepot ON t1.from_depot_id = fromDepot.id
            LEFT JOIN crm_depot toDepot ON t1.to_depot_id = toDepot.id
            LEFT JOIN crm_express_company expressCompany ON t1.express_company_id = expressCompany.id
        WHERE
            t1.enabled=1
        """)
        if(StringUtils.isNotBlank(expressOrderQuery.extendBusinessId)){
            sb.append("""  and t1.extend_business_id = :extendBusinessId  """)
        }
        if(expressOrderQuery.printDateStart != null){
            sb.append("""  and t1.print_date >= :printDateStart  """)
        }
        if(expressOrderQuery.printDateEnd != null){
            sb.append("""   and t1.print_date <  :printDateEnd  """)
        }
        if(StringUtils.isNotBlank(expressOrderQuery.extendBusinessIdStart)){
            sb.append("""  and t1.extend_business_id >= :extendBusinessIdStart  """)
        }
        if(StringUtils.isNotBlank(expressOrderQuery.extendType)){
            sb.append("""  and t1.extend_type = :extendType  """)
        }
        if(expressOrderQuery.outPrint !=null && expressOrderQuery.outPrint ){
            sb.append("""  and t1.out_print_date is not null  """)
        }
        if(expressOrderQuery.outPrint !=null && !expressOrderQuery.outPrint ){
            sb.append("""  and t1.out_print_date is  null  """)
        }
        if(expressOrderQuery.expressPrint !=null && expressOrderQuery.expressPrint ){
            sb.append("""  and t1.express_print_date is not null  """)
        }
        if(expressOrderQuery.expressPrint !=null && !expressOrderQuery.expressPrint ){
            sb.append("""  and t1.express_print_date is  null  """)
        }
        if(StringUtils.isNotBlank(expressOrderQuery.fromDepotName)){
            sb.append("""  and fromDepot.name like concat('%',:fromDepotName,'%') """)
        }
        if(StringUtils.isNotBlank(expressOrderQuery.toDepotName)){
            sb.append("""  and toDepot.name like concat('%', :toDepotName,'%')   """)
        }
        if(StringUtils.isNotBlank(expressOrderQuery.expressCompanyName)){
            sb.append("""  and expressCompany.name like concat('%',:expressCompanyName,'%')  """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(expressOrderQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ExpressOrderDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)

    }


}