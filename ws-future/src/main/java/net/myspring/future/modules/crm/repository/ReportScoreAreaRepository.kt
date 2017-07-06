package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ReportScoreArea
import net.myspring.future.modules.crm.dto.ReportScoreAreaDto
import net.myspring.future.modules.crm.dto.ReportScoreOfficeDto
import net.myspring.future.modules.crm.web.query.ReportScoreAreaQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface ReportScoreAreaRepository : BaseRepository<ReportScoreArea, String>,ReportScoreAreaRepositoryCustom {
}
    interface ReportScoreAreaRepositoryCustom{
        fun findPage(pageable: Pageable, reportScoreAreaQuery: ReportScoreAreaQuery): Page<ReportScoreAreaDto>
    }


class ReportScoreAreaRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ReportScoreAreaRepositoryCustom{
    override fun findPage(pageable: Pageable, reportScoreAreaQuery: ReportScoreAreaQuery): Page<ReportScoreAreaDto> {
        val sb = StringBuilder("""
        SELECT
        t1.*
        FROM
        crm_report_score_area t1
        WHERE
        t1.date_rank >0
        and t1.month_rank >0
        """)
        if(StringUtils.isNotEmpty(reportScoreAreaQuery.areaId))
            sb.append("""  and t1.office_id =:areaId """)
        if(reportScoreAreaQuery.scoreDateStart!=null)
            sb.append("""  and t1.score_date  >=:scoreDateStart """)
        if(reportScoreAreaQuery.scoreDateEnd!=null)
            sb.append("""  and t1.score_date  <:scoreDateEnd """)
        if(CollectionUtil.isNotEmpty(reportScoreAreaQuery.officeIdList))
            sb.append("""  and t1.office_id in (:officeIdList) """)
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(reportScoreAreaQuery), BeanPropertyRowMapper(ReportScoreAreaDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(reportScoreAreaQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
