package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.BankIn
import net.myspring.future.modules.crm.domain.StoreAllot
import net.myspring.future.modules.crm.domain.StoreAllotIme
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
import kotlin.collections.HashMap


interface StoreAllotImeRepository : BaseRepository<StoreAllotIme, String>, StoreAllotImeRepositoryCustom {

    fun findByProductImeId(productImeId: String): MutableList<StoreAllotIme>

}

interface StoreAllotImeRepositoryCustom{

    fun findPage(pageable: Pageable, storeAllotQuery: StoreAllotQuery): Page<StoreAllotDto>

    fun findByStoreAllotId(storeAllotId: String): MutableList<StoreAllotImeDto>

    fun findDtoListByStoreAllotIdList(storeAllotIdList: MutableList<String>, limit: Int): MutableList<StoreAllotImeDto>

}

class StoreAllotImeRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): StoreAllotImeRepositoryCustom{
    override fun findDtoListByStoreAllotIdList(storeAllotIdList: MutableList<String>, limit: Int): MutableList<StoreAllotImeDto> {
        val params = HashMap<String, Any>()
        params.put("storeAllotIdList", storeAllotIdList)
        params.put("limit", limit)
        return namedParameterJdbcTemplate.query("""
        SELECT t1.business_id storeAllotBusinessId, t1.out_code storeAllotOutCode,
                    t1.bill_date storeAllotBillDate, t1.from_store_id storeAllotFromStoreId, t1.to_store_id storeAllotToStoreId,
                    toStore.office_id storeAllotToStoreOfficeId, t3.ime productImeIme, t3.meid productImeMeid, t2.*
        FROM  crm_store_allot t1, crm_store_allot_ime t2, crm_product_ime t3, crm_depot toStore
        where t1.enabled=1
                  AND t1.id = t2.store_allot_id
                  AND t2.enabled = 1
                  AND t2.product_ime_id = t3.id
                  AND t3.enabled = 1
                  AND t1.to_store_id = toStore.id
                  AND toStore.enabled = 1
                  AND t1.id in (:storeAllotIdList)
        ORDER BY t1.id, t2.created_date
        limit 0, :limit
          """, params, MyBeanPropertyRowMapper(StoreAllotImeDto::class.java))
    }

    override  fun findByStoreAllotId(storeAllotId: String): MutableList<StoreAllotImeDto>{
        return namedParameterJdbcTemplate.query("""
         SELECT
            ime.ime productImeIme,
            ime.meid productImeMeid,
            product.name productName,
            t1.*
        FROM
            crm_store_allot_ime t1,
            crm_product product,
            crm_product_ime ime
        WHERE
            t1.store_allot_id = :storeAllotId
            AND t1.enabled = 1
            AND t1.product_id = product.id
            AND t1.product_ime_id = ime.id
          """, Collections.singletonMap("storeAllotId", storeAllotId), MyBeanPropertyRowMapper(StoreAllotImeDto::class.java))
    }

    override fun findPage(pageable: Pageable, storeAllotQuery: StoreAllotQuery): Page<StoreAllotDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}