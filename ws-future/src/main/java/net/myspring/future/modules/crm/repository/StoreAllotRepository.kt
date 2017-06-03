package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.StoreAllot
import net.myspring.future.modules.crm.dto.StoreAllotDto
import net.myspring.future.modules.crm.web.query.StoreAllotQuery
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


interface StoreAllotRepository : BaseRepository<StoreAllot, String>, StoreAllotRepositoryCustom {

    @Query("""
    SELECT
        MAX(t1.businessId)
    FROM
        #{#entityName} t1
    WHERE
        t1.createdDate >= ?1
        """)
    fun findMaxBusinessId(localDateTime: LocalDateTime): String


}


interface StoreAllotRepositoryCustom{

    fun findPage(pageable: Pageable, storeAllotQuery: StoreAllotQuery): Page<StoreAllotDto>

    fun findDto(id: String): StoreAllotDto

}

class StoreAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): StoreAllotRepositoryCustom{
    override fun findDto(id: String): StoreAllotDto {
        return namedParameterJdbcTemplate.queryForObject("""
            SELECT
                t2.express_codes expressOrderExpressCodes,
                t2.express_company_id,
                t1.*
            FROM
                crm_store_allot t1
                LEFT JOIN crm_express_order t2 ON t1.express_order_id = t2.id
            WHERE
                t1.enabled = 1
                AND t1.id =  :id
                """, Collections.singletonMap("id", id), MyBeanPropertyRowMapper(StoreAllotDto::class.java))
    }

    override fun findPage(pageable: Pageable, storeAllotQuery: StoreAllotQuery): Page<StoreAllotDto> {

        val sb = StringBuffer()
        sb.append("""
        SELECT  t1.*
        FROM  crm_store_allot t1
        where t1.enabled=1
        """)
        if(StringUtils.isNotBlank(storeAllotQuery.remarks)){
            sb.append("""   and t1.remarks like concat('%', :remarks,'%')  """)
        }
        if(StringUtils.isNotBlank(storeAllotQuery.status)){
            sb.append("""   and t1.status= :status  """)
        }
        if(StringUtils.isNotBlank(storeAllotQuery.createdBy)){
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if(StringUtils.isNotBlank(storeAllotQuery.fromStoreId)){
            sb.append("""  and t1.from_store_id= :fromStoreId  """)
        }
        if(StringUtils.isNotBlank(storeAllotQuery.toStoreId)){
            sb.append("""  and t1.to_store_id= :toStoreId  """)
        }
        if(storeAllotQuery.businessIdList != null){
            sb.append("""  and t1.business_id  in  (:businessIdList)  """)
        }
        if(StringUtils.isNotBlank(storeAllotQuery.outCode)){
            sb.append("""  and t1.out_code like concat('%', :outCode, '%')  """)
        }
        if(storeAllotQuery.createdDateStart != null){
            sb.append("""  and t1.created_date >= :createdDateStart """)
        }
        if(storeAllotQuery.createdDateEnd != null){
            sb.append("""  and t1.created_date <  :createdDateEnd """)
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(storeAllotQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(StoreAllotDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)

    }


}