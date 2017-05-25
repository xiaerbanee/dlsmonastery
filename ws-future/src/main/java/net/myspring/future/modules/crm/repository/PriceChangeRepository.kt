package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.PriceChange
import net.myspring.future.modules.crm.domain.PriceChangeProduct

import net.myspring.future.modules.crm.domain.PricesystemChange
import net.myspring.future.modules.crm.dto.PriceChangeDto
import net.myspring.future.modules.crm.web.query.PriceChangeQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.text.StringUtils

import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import org.apache.poi.ss.formula.functions.T
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import javax.persistence.EntityManager


interface PriceChangeRepository : BaseRepository<PriceChange, String>,PriceChangeRepositoryCustom {

    @Query("""
    SELECT t1.*
    FROM crm_price_change t1
    WHERE
        t1.upload_end_date>=?1
        and t1.enabled=1
        ORDER BY  t1.id DESC
        """, nativeQuery = true)
    fun finAllByEnabled(uploadEndDate: LocalDateTime): MutableList<PriceChange>


    @Query("""
    SELECT t1.*
    FROM crm_price_change t1
    ORDER BY  t1.price_change_date DESC ,t1.upload_end_date DESC
    limit 0,1
        """, nativeQuery = true)
    fun findNearPriceChange(): PriceChange

    @Query("""
    SELECT t1.*
    FROM crm_price_change t1
    WHERE
        and t1.enabled=1
    ORDER BY  t1.id DESC
        """, nativeQuery = true)
    fun findAllEnabled(): MutableList<PriceChange>

}

interface PriceChangeRepositoryCustom{
    fun findPage(pageable: Pageable, priceChangeQuery : PriceChangeQuery): Page<PriceChangeDto>
}

class PriceChangeRepositoryImpl @Autowired constructor(val entityManager: EntityManager):PriceChangeRepositoryCustom{
    override fun findPage(pageable: Pageable, priceChangeQuery: PriceChangeQuery): Page<PriceChangeDto> {
        val sb = StringBuffer()
        sb.append("""
         SELECT
            group_concat(DISTINCT productType.name) productTypeName, t1.*
        FROM
            crm_price_change t1,crm_price_change_product product,crm_product_type productType
        WHERE
            t1.enabled=1
            AND product.product_type_id = productType.id
            AND product.price_change_id = t1.id
        """)
        if (StringUtils.isNotEmpty(priceChangeQuery.name)) {
            sb.append("""
                AND t1.name LIKE CONCAT('%', :name, '%')
            """)
        }
        sb.append("""
                GROUP BY product.price_change_id
            """)
        val query = entityManager.createNativeQuery(sb.toString(), PriceChangeDto::class.java)
        query.setParameter("name", priceChangeQuery.name)

        return query.resultList as Page<PriceChangeDto>

    }


}