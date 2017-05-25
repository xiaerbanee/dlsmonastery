package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.PriceChange
import net.myspring.future.modules.crm.domain.PriceChangeIme
import net.myspring.future.modules.crm.domain.PriceChangeProduct

import net.myspring.future.modules.crm.domain.PricesystemChange
import net.myspring.future.modules.crm.dto.PriceChangeImeDto
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery
import org.apache.ibatis.annotations.Param
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime


interface PriceChangeImeRepository : BaseRepository<PriceChangeIme, String> {

    fun findByPriceChangeId(priceChangeId: String): List<PriceChangeIme>


    @Query("""
    SELECT
        t1.*
    FROM
        crm_price_change_ime t1
    WHERE
        t1.enabled = 1
        AND t1.price_change_id = ?1 and  t1.uploadDate is not null
        """, nativeQuery = true)
    fun findByPriceChangeIdAndUploadDateIsNotNull(priceChangeId: String): List<PriceChangeIme>




}
