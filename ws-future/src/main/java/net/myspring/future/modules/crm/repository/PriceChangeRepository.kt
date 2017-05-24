package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.PriceChange
import net.myspring.future.modules.crm.domain.PriceChangeProduct

import net.myspring.future.modules.crm.domain.PricesystemChange

import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import org.apache.poi.ss.formula.functions.T




interface PriceChangeRepository : BaseRepository<PriceChange, String> {

    @Query("""
    SELECT t1.*
    FROM crm_price_change t1
    WHERE
        t1.upload_end_date>=?1
        and t1.enabled=1
        ORDER BY  t1.id DESC
        """, nativeQuery = true)
    fun finAllByEnabled(uploadEndDate: LocalDateTime): List<PriceChange>


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
    fun findAllEnabled(): List<PriceChange>


}
