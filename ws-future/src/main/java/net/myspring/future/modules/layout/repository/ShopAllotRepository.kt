package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopAllot
import net.myspring.future.modules.layout.dto.ShopAllotDto
import net.myspring.future.modules.layout.web.query.ShopAllotQuery
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

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopAllotRepository : BaseRepository<ShopAllot,String>,ShopAllotRepositoryCustom{

    @Query("""
    select
        max(t.businessId)
    from
        #{#entityName} t
    where  t.createdDate >= ?1
    """)
    fun findMaxBusinessId(localDate: LocalDate): String


}

interface ShopAllotRepositoryCustom{
    fun findPage(pageable: Pageable, shopAllotQuery: ShopAllotQuery): Page<ShopAllotDto>
    fun findShopAllotDto(id: String): ShopAllotDto
}

class ShopAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate ):ShopAllotRepositoryCustom{
    override fun findShopAllotDto(id: String): ShopAllotDto {
        return namedParameterJdbcTemplate.queryForObject("""
       SELECT
            sum(t2.qty * t2.sale_price) saleTotalPrice,
            sum(t2.qty * t2.return_price) returnTotalPrice,
            t1.*
        FROM
            crm_shop_allot t1,
            crm_shop_allot_detail t2
        WHERE
            t1.enabled = 1
        AND t1.id = :id
        AND t1.id = t2.shop_allot_id
        GROUP BY
            t1.id
          """, Collections.singletonMap("id", id), BeanPropertyRowMapper(ShopAllotDto::class.java))
    }

    override fun findPage(pageable: Pageable, shopAllotQuery: ShopAllotQuery): Page<ShopAllotDto> {

        val sb = StringBuffer()
        sb.append("""
        SELECT  t1.*
        FROM  crm_shop_allot t1
        where t1.enabled=1
        """)
        if(StringUtils.isNotBlank(shopAllotQuery.fromShopId)){
            sb.append("""   and t1.from_shop_id= :fromShopId  """)
        }
        if(StringUtils.isNotBlank(shopAllotQuery.toShopId)){
            sb.append("""  and t1.to_shop_id= :toShopId  """)
        }
        if(StringUtils.isNotBlank(shopAllotQuery.status)){
            sb.append("""  and t1.status= :status  """)
        }
        if(StringUtils.isNotBlank(shopAllotQuery.businessId)){
            sb.append("""  and t1.business_id like concat('%', :businessId,'%') """)
        }
        if(shopAllotQuery.businessIdList!=null){
            sb.append("""  and t1.business_id IN  (:businessIdList) """)
        }
        if(shopAllotQuery.createdDateStart != null){
            sb.append("""  AND t1.created_date >= :createdDateStart """)
        }
        if(shopAllotQuery.createdDateEnd != null){
            sb.append("""  AND t1.created_date < :createdDateEnd """)
        }
        if(shopAllotQuery.auditDateStart != null){
            sb.append("""  AND t1.audit_date >= :auditDateStart """)
        }
        if(shopAllotQuery.auditDateEnd != null){
            sb.append("""   AND t1.audit_date < :auditDateEnd """)
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(shopAllotQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ShopAllotDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)

    }
}