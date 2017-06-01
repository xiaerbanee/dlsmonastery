package net.myspring.future.modules.crm.repository

import com.ctc.wstx.util.StringUtil
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.*
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.dto.ReportScoreOfficeDto
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
import net.myspring.future.modules.crm.web.query.ReportScoreOfficeQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate


interface ReportScoreOfficeRepository : BaseRepository<ReportScoreOffice, String>, ReportScoreOfficeRepositoryCustom{
}

interface ReportScoreOfficeRepositoryCustom{
    fun findPage(pageable: Pageable, reportScoreOfficeQuery: ReportScoreOfficeQuery ): Page<ReportScoreOfficeDto>
}

class ReportScoreOfficeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ReportScoreOfficeRepositoryCustom{
    override fun findPage(pageable: Pageable, reportScoreOfficeQuery: ReportScoreOfficeQuery): Page<ReportScoreOfficeDto> {
        val sb = StringBuilder("""
        SELECT
        t1.*
        FROM
        crm_report_score_office t1
        WHERE
        t1.date_rank >0
        and t1.month_rank >0
        """)
//        if(StringUtils.isNotEmpty(reportScoreOfficeQuery.officeId))
//            sb.append("""  and t1.office_id =:officeId""")
//        if(StringUtils.isNotEmpty(reportScoreOfficeQuery.scoreDate.toString()))
//            sb.append("""  and t1.score_date  =:scoreDate """)
//        if(StringUtils.isNotEmpty(reportScoreOfficeQuery.areaId))
//            sb.append("""  and t1.""")
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(reportScoreOfficeQuery), BeanPropertyRowMapper(ReportScoreOfficeDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(reportScoreOfficeQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}