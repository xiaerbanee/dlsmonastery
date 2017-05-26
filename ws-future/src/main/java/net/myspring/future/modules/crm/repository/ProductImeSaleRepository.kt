package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.*
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
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import net.myspring.common.dto.NameValueDto
import net.myspring.future.modules.crm.domain.ProductImeSale




interface ProductImeSaleRepository : BaseRepository<ProductImeSale, String> {

    fun findByProductImeId(productImeId: String): MutableList<ProductImeSale>

    @Query("""
    SELECT
        t1
    FROM
        #{#entityName} t1
    WHERE
        t1.employeeId = :employeeId
    AND t1.createdDate >= :createdDateStart
    AND t1.createdDate <= :createdDateEnd
        """ )
    fun findByEmployeeId(@Param("employeeId") employeeId: String, @Param("createdDateStart") createdDateStart: LocalDateTime, @Param("createdDateEnd") createdDateEnd: LocalDateTime): MutableList<ProductImeSale>


}
