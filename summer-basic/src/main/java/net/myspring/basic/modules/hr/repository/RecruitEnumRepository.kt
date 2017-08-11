package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.RecruitEnum
import net.myspring.basic.modules.hr.dto.RecruitDto
import net.myspring.basic.modules.hr.dto.RecruitEnumDto
import net.myspring.basic.modules.hr.web.query.RecruitEnumQuery
import net.myspring.basic.modules.hr.web.query.RecruitQuery
import net.myspring.util.repository.MySQLDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

interface RecruitEnumRepository : BaseRepository<RecruitEnum, String>,RecruitEnumRepositoryCustom {

}

interface RecruitEnumRepositoryCustom {
    fun findPage(pageable: Pageable, recruitEnumQuery:RecruitEnumQuery): Page<RecruitEnumDto>

}

class RecruitEnumRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): RecruitEnumRepositoryCustom{
    override fun findPage(pageable: Pageable, recruitEnumQuery: RecruitEnumQuery): Page<RecruitEnumDto> {
        var sb = StringBuilder("""
                select t1.*
                from hr_recruit_enum t1
                where t1.enabled=1
        """);
        if(recruitEnumQuery.category!=null){
            sb.append("""
                   AND t1.category =:category
                """);
        }
        if(recruitEnumQuery.value!=null){
            sb.append("""
                   AND t1.value LIKE CONCAT ('%',:value,'%')
                """);
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(recruitEnumQuery), BeanPropertyRowMapper(RecruitEnumDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(recruitEnumQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

}