package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.OfficeChange
import net.myspring.basic.modules.hr.dto.DutyLeaveDto
import net.myspring.basic.modules.hr.dto.OfficeChangeDto
import net.myspring.basic.modules.hr.dto.OfficeChangeFormDto
import net.myspring.basic.modules.hr.web.query.DutyLeaveQuery
import net.myspring.basic.modules.hr.web.query.OfficeChangeQuery
import net.myspring.util.repository.MySQLDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Created by lihx on 2017/5/25.
 */
interface OfficeChangeRepository : BaseRepository<OfficeChange,String>,OfficeChangeRepositoryCustom{

}
interface OfficeChangeRepositoryCustom{
    fun findPage(pageable: Pageable, officeChangeQuery: OfficeChangeQuery): Page<OfficeChangeDto>

}
class OfficeChangeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):OfficeChangeRepositoryCustom {

    override fun findPage(pageable: Pageable, officeChangeQuery: OfficeChangeQuery): Page<OfficeChangeDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*,office.name
            FROM
            hr_office_change t1,sys_office office
            WHERE
            t1.office_id=office.id
            AND t1.enabled=1
        """)
        if (officeChangeQuery.type != null) {
            sb.append(" AND t1.type=:type ")
        }
        if (officeChangeQuery.createdDateStart != null) {
            sb.append(" AND t1.created_date >= :createdDateStart ")
        }
        if (officeChangeQuery.createdDateEnd != null) {
            sb.append(" AND t1.created_date  < :createdDateEnd ")
        }
        if (officeChangeQuery.officeId != null) {
            sb.append(" AND t1.office_id=:officeId ")
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(), pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(officeChangeQuery), BeanPropertyRowMapper(OfficeChangeDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(officeChangeQuery), Long::class.java);
        return PageImpl(list, pageable, count);
    }
}