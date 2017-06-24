package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.StoreAllot
import net.myspring.future.modules.crm.dto.StoreAllotDto
import net.myspring.future.modules.crm.web.query.StoreAllotQuery
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


interface StoreAllotRepository : BaseRepository<StoreAllot, String>, StoreAllotRepositoryCustom {

    @Query("""
    SELECT
        MAX(t1.businessId)
    FROM
        #{#entityName} t1
    WHERE
        t1.createdDate >= ?1
        """)
    fun findMaxBusinessId(createdDate: LocalDateTime): String


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
                t2.out_print_date expressOrderOutPrintDate,
                t2.express_print_date expressOrderExpressPrintDate,
                toStore.contator toStoreContator,
                toStore.mobile_phone toStoreMobilePhone,
                toStore.address toStoreAddress,
                t1.*
            FROM
                crm_store_allot t1
                LEFT JOIN crm_express_order t2 ON t1.express_order_id = t2.id
                LEFT JOIN crm_depot toStore ON t1.to_store_id = toStore.id
            WHERE
                t1.enabled = 1
                AND t1.id =  :id

                """, Collections.singletonMap("id", id), MyBeanPropertyRowMapper(StoreAllotDto::class.java))
    }

    override fun findPage(pageable: Pageable, storeAllotQuery: StoreAllotQuery): Page<StoreAllotDto> {

        val sb = StringBuffer()
        sb.append("""
        SELECT
            t2.express_codes expressOrderExpressCodes,
            t2.express_company_id,
            t2.out_print_date expressOrderOutPrintDate,
            t2.express_print_date expressOrderExpressPrintDate,
            t1.*
        FROM
            crm_store_allot t1
             LEFT JOIN crm_express_order t2 ON t1.express_order_id = t2.id
        WHERE
            t1.enabled = 1
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
        if(CollectionUtil.isNotEmpty(storeAllotQuery.businessIdList) ){
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
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(storeAllotQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(StoreAllotDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)

    }


}