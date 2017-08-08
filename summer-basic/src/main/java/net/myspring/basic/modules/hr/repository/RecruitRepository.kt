package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Recruit
import net.myspring.basic.modules.hr.dto.PositionDto
import net.myspring.basic.modules.hr.dto.RecruitDto
import net.myspring.basic.modules.hr.web.query.RecruitQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


/**
 * Created by lihx on 2017/5/24.
 */
interface RecruitRepository : BaseRepository<Recruit, String>,RecruitRepositoryCustom {
   
}

interface RecruitRepositoryCustom{
    fun findPage(pageable: Pageable, recruitQuery: RecruitQuery): Page<RecruitDto>
}

class RecruitRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): RecruitRepositoryCustom{
    override fun findPage(pageable: Pageable, recruitQuery: RecruitQuery): Page<RecruitDto> {
        var sb = StringBuilder("""
                select t1.*
                from hr_recruit t1
                where t1.enabled=1
        """);

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(recruitQuery), BeanPropertyRowMapper(RecruitDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(recruitQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

}
