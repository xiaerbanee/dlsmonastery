package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ReportScore
import net.myspring.future.modules.crm.dto.ProductImeDto
import net.myspring.future.modules.crm.dto.ReportScoreDto
import net.myspring.future.modules.crm.web.query.ReportScoreQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.util.*


interface ReportScoreRepository : BaseRepository<ReportScore, String>, ReportScoreRepositoryCustom{

    fun findByScoreDateAndCompanyId(scoreDate : LocalDate, companyId: String): ReportScore?
}


interface ReportScoreRepositoryCustom{

    fun findPage(pageable: Pageable, reportScoreQuery: ReportScoreQuery): Page<ReportScoreDto>

    fun findDto(id: String): ReportScoreDto
}

class ReportScoreRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ReportScoreRepositoryCustom{
    override fun findDto(id: String): ReportScoreDto {
        return namedParameterJdbcTemplate.queryForObject("""
        select t1.*
        from crm_report_score t1
        where t1.enabled = 1 and t1.id = :id
                """, Collections.singletonMap("id", id), BeanPropertyRowMapper(ReportScoreDto::class.java))

    }

    override fun findPage(pageable: Pageable, reportScoreQuery: ReportScoreQuery): Page<ReportScoreDto> {

        val sb = StringBuffer()
        sb.append("""
        select t1.*
        from crm_report_score t1
        where t1.enabled = 1
        """)
        if(reportScoreQuery.scoreDateStart != null){
            sb.append("""  AND t1.score_date >= :scoreDateStart """)
        }
        if(reportScoreQuery.scoreDateEnd != null){
            sb.append("""  AND t1.score_date < :scoreDateEnd """)
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(reportScoreQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ReportScoreDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)


    }


}