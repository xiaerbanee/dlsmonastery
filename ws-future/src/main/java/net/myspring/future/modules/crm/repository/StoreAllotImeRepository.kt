package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.StoreAllotIme
import net.myspring.future.modules.crm.dto.StoreAllotImeDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface StoreAllotImeRepository : BaseRepository<StoreAllotIme, String>, StoreAllotImeRepositoryCustom {
}

interface StoreAllotImeRepositoryCustom{

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
          """, params, BeanPropertyRowMapper(StoreAllotImeDto::class.java))
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
          """, Collections.singletonMap("storeAllotId", storeAllotId), BeanPropertyRowMapper(StoreAllotImeDto::class.java))
    }

}