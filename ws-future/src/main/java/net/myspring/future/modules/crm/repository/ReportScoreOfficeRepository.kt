package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ReportScoreOffice
import net.myspring.future.modules.crm.dto.ReportScoreOfficeDto
import net.myspring.future.modules.crm.web.query.ReportScoreOfficeQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


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
//        if(reportScoreOfficeQuery.officeId !=null)
//            sb.append("""  and t1.office_id =:officeId""")
//        if(reportScoreOfficeQuery.scoreDate!=null)
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