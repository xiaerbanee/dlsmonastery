package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.AfterSale
import net.myspring.future.modules.crm.domain.AfterSaleDetail
import net.myspring.future.modules.crm.domain.AfterSaleFlee
import net.myspring.future.modules.crm.domain.BankIn
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.web.query.AfterSaleQuery
import net.myspring.future.modules.crm.web.query.BankInQuery
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime


interface AfterSaleDetailRepository : BaseRepository<AfterSaleDetail, String> {


     fun findByAfterSaleIdInAndType(afterSaleId: MutableList<String>, type: String): MutableList<AfterSaleDetail>


}
