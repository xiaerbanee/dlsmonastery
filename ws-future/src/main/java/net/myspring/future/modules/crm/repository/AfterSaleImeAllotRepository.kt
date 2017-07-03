package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSale
import net.myspring.future.modules.crm.domain.AfterSaleImeAllot
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


interface AfterSaleImeAllotRepository : BaseRepository<AfterSaleImeAllot, String>,AfterSaleImeAllotRepositoryCustom {
    fun findByAfterSaleId(afterSaleId:String):MutableList<AfterSaleImeAllot>
}

interface AfterSaleImeAllotRepositoryCustom{
}

class AfterSaleImeAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AfterSaleImeAllotRepositoryCustom {
}