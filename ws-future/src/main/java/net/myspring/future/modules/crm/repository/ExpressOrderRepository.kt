package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ExpressOrder

import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.dto.ExpressDto
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import net.myspring.future.modules.crm.dto.PriceChangeDto
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery
import net.myspring.future.modules.crm.web.query.PriceChangeQuery
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


interface ExpressOrderRepository : BaseRepository<ExpressOrder, String>, ExpressOrderRepositoryCustom {

    fun findByExtendIdAndExtendType(extendId: String, extendType: String): ExpressOrder

}

interface ExpressOrderRepositoryCustom{
    fun findPage(pageable : Pageable, expressOrderQuery : ExpressOrderQuery): Page<ExpressOrderDto>

    fun findDtoByGoodsOrderId(goodsOrderId: String): ExpressOrderDto?

    fun findDto(id: String): ExpressOrderDto
}

class ExpressOrderRepositoryImpl @Autowired constructor( val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ExpressOrderRepositoryCustom {
    override fun findDto(id: String): ExpressOrderDto {

        return namedParameterJdbcTemplate.queryForObject("""
       SELECT
            t1.*
        FROM
            crm_express_order t1
        WHERE
            t1.enabled=1 and t1.id = :id
          """, Collections.singletonMap("id", id), MyBeanPropertyRowMapper(ExpressOrderDto::class.java))
    }

    override fun findDtoByGoodsOrderId(goodsOrderId: String): ExpressOrderDto? {

        val result =  namedParameterJdbcTemplate.query("""
        SELECT
            t1.*
        FROM
            crm_express_order t1, crm_goods_order t2
        WHERE
            t1.enabled=1  and t1.id = t2.express_order_id
            and t2.id = :goodsOrderId
          """, Collections.singletonMap("goodsOrderId", goodsOrderId), MyBeanPropertyRowMapper(ExpressOrderDto::class.java))

        if(result.size >= 1){
            return result[0]
        }else {
            return null
        }
    }

    override fun findPage(pageable : Pageable,expressOrderQuery: ExpressOrderQuery): Page<ExpressOrderDto> {
        val sb = StringBuffer()
        sb.append("""
        SELECT
            t1.*
        FROM
            crm_express_order t1
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
            sb.append("""
                 and t1.from_depot_id in(
                                                    select fromDepot.id
                                                    from crm_depot fromDepot
                                                    where fromDepot.name like concat('%',:fromDepotName,'%')
                                                              and fromDepot.enabled=1
                                                     )
            """)
        }
        if(StringUtils.isNotBlank(expressOrderQuery.toDepotName)){
            sb.append("""
                 and t1.to_depot_id in(
                                                select toDepot.id
                                                from crm_depot toDepot
                                                where toDepot.name like concat('%', :toDepotName,'%')
                                                          and toDepot.enabled=1
                                                )
            """)
        }
        if(StringUtils.isNotBlank(expressOrderQuery.expressCompanyName)){
            sb.append("""
                  and t1.express_company_id in(
                                                select expressCompany.id
                                                from crm_express_company expressCompany
                                                where expressCompany.name like concat('%',:expressCompanyName,'%')
                                                          and expressCompany.enabled=1
                                                )
            """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(expressOrderQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ExpressOrderDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)

    }


}