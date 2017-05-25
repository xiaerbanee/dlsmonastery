package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.*
import net.myspring.future.modules.crm.dto.*
import net.myspring.future.modules.crm.web.query.BankInQuery
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import net.myspring.future.modules.crm.web.query.StoreAllotQuery
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate


interface StoreAllotDetailRepository : BaseRepository<StoreAllotDetail, String> {


    @Query("""
    SELECT product.name productName, product.out_id outId, product.has_ime productHasIme, 0 shipQty, t1.*
    FROM  crm_store_allot_detail t1, crm_product product
    WHERE t1.product_id = product.id
        AND  t1.store_allot_id in ?1
        ORDER BY t1.store_allot_id, product.has_ime DESC
        """, nativeQuery = true)
    fun findByStoreAllotIds(storeAllotIds: MutableList<String>): MutableList<StoreAllotDetailDto>

    fun deleteByStoreAllotId(storeAllotId: String)


    @Query("""
        SELECT result.productId productId, SUM(result.billQty) billQty
        FROM
            (
                ( SELECT detail.product_id productId, SUM(detail.bill_qty) billQty
                  FROM  crm_goods_order_detail detail
                  WHERE detail.goods_order_id IN ( SELECT t1.id FROM  crm_goods_order t1 WHERE t1.bill_date = :billDate AND t1.store_id = :toStoreId AND t1. STATUS = :status AND t1.enabled = 1 )
                  GROUP BY detail.product_id  )
               UNION ALL
                ( SELECT  ime.product_id productId, COUNT(*) billQty
                  FROM crm_goods_order_ime ime
                  WHERE  ime.goods_order_id IN ( SELECT t2.id FROM  crm_goods_order t2 WHERE t2.bill_date = :billDate AND t2.store_id = :toStoreId AND t2. STATUS = :status AND t2.enabled = 1 )
                  GROUP BY ime.product_id )
                UNION ALL
                ( SELECT  t1.id productId, 0 billQty
                  FROM crm_product t1
                  WHERE  t1.enabled=1 AND t1.company_id = :companyId )
            ) result
        GROUP BY result.productId
        ORDER BY result.billQty DESC
        """, nativeQuery = true)
    fun findStoreAllotDetailsForFastAllot(@Param("billDate") billDate: LocalDate, @Param("toStoreId") toStoreId: String, @Param("status") status: String, @Param("companyId") companyId: String): MutableList<SimpleStoreAllotDetailDto>

    @Query("""
    SELECT
        t1.id productId
    FROM
        crm_product t1
    WHERE
        t1.enabled = 1
        AND t1.company_id = ?1
        """, nativeQuery = true)
    fun findStoreAllotDetailListForNew(companyId: String): MutableList<SimpleStoreAllotDetailDto>


}
