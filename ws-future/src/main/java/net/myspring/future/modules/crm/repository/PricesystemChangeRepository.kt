package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository

import net.myspring.future.modules.crm.domain.PricesystemChange
import net.myspring.future.modules.crm.dto.PriceChangeImeDto
import net.myspring.future.modules.crm.dto.PricesystemChangeDto
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery
import net.myspring.future.modules.crm.web.query.PricesystemChangeQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.persistence.EntityManager


interface PricesystemChangeRepository : BaseRepository<PricesystemChange, String>, PricesystemChangeRepositoryCustom {




}


interface PricesystemChangeRepositoryCustom{
    fun findPage(pageable : Pageable, pricesystemChangeQuery : PricesystemChangeQuery): Page<PricesystemChangeDto>?

}

class PricesystemChangeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): PricesystemChangeRepositoryCustom {
    override fun findPage(pageable : Pageable, pricesystemChangeQuery: PricesystemChangeQuery): Page<PricesystemChangeDto> {
        val sb = StringBuffer()
        sb.append("""
        SELECT
            t1.*
        FROM
            crm_pricesystem_change t1
        WHERE
            t1.enabled=1
        """)
        if(StringUtils.isNotBlank(pricesystemChangeQuery.productId)){
            sb.append("""  and t1.product_id=:productId  """)
        }
        if(StringUtils.isNotBlank(pricesystemChangeQuery.pricesystemId)){
            sb.append("""  and t1.pricesystem_id =:pricesystemId """)
        }
        if(StringUtils.isNotBlank(pricesystemChangeQuery.status)){
            sb.append("""  and t1.status = :status  """)
        }
        if(pricesystemChangeQuery.createdDateStart != null){
            sb.append("""  and t1.created_date >= :createdDateStart  """)
        }
        if(pricesystemChangeQuery.createdDateEnd !=null){
            sb.append("""  and t1.created_date < :createdDateEnd  """)
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(pricesystemChangeQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(PricesystemChangeDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)

    }


}