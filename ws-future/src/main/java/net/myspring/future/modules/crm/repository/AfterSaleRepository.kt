package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSale
import net.myspring.future.modules.crm.dto.AfterSaleDto
import net.myspring.future.modules.crm.web.query.AfterSaleQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.util.*


interface AfterSaleRepository : BaseRepository<AfterSale, String>,AfterSaleRepositoryCustom {

    @Query("""
    SELECT t1
    FROM #{#entityName} t1, ProductIme t2
    WHERE t1.badProductImeId=t2.id
        AND t2.ime in ?1
        """)
    fun findByBadProductImeIn(imeList: MutableList<String>): MutableList<AfterSale>

    @Query("""
    SELECT MAX(t1.businessId)
    FROM #{#entityName} t1
    WHERE t1.createdDate >= ?1
        """)
    fun findMaxBusinessId(dateStart: LocalDate): String

}


interface AfterSaleRepositoryCustom{
    fun findPage(pageable: Pageable, afterSaleQuery : AfterSaleQuery): Page<AfterSale>?

    fun findDtoByBadProductImeIn(imeList: MutableList<String>): MutableList<AfterSaleDto>
}

class AfterSaleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AfterSaleRepositoryCustom {
    override fun findDtoByBadProductImeIn(imeList: MutableList<String>): MutableList<AfterSaleDto> {
        val sb = StringBuilder()
        sb.append("""
             SELECT
                 t1.*,t2.ime as 'badProductIme',t3.name as 'badProductName',t4.name as 'badDepotName'
             FROM
                 crm_after_sale left join crm_product_ime t2 on t1.bad_product_ime_id=t2.id
                 left join crm_product t3 on t1.bad_product_id=t3.id
                 left join crm_depot t4 on t1.bad_depot_id=t4.id
             WHERE
                 t1.enabled=1
                AND t2.ime in (:imeList)
        """)
        var paramMap = HashMap<String, Any>()
        paramMap.put("imeList", imeList);
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(AfterSaleDto::class.java))
    }

    override fun findPage(pageable: Pageable, afterSaleQuery: AfterSaleQuery): Page<AfterSale>? {
        return null
    }


}