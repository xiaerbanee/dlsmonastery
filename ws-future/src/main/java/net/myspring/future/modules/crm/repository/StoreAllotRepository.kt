package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.BankIn
import net.myspring.future.modules.crm.domain.StoreAllot
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.web.query.BankInQuery
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import net.myspring.future.modules.crm.web.query.StoreAllotQuery
import net.myspring.future.modules.crm.dto.StoreAllotImeDto
import net.myspring.future.modules.crm.dto.StoreAllotDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
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
    fun findMaxBusinessId(localDate: LocalDate): String


}


interface StoreAllotRepositoryCustom{

    fun findPage(pageable: Pageable, storeAllotQuery: StoreAllotQuery): Page<StoreAllotDto>

    fun findStoreAllotDtoById(id: String): StoreAllotDto

}

class StoreAllotRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): StoreAllotRepositoryCustom{
    override fun findStoreAllotDtoById(id: String): StoreAllotDto {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}