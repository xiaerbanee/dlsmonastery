package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Recruit
import net.myspring.basic.modules.hr.dto.PositionDto
import net.myspring.basic.modules.hr.dto.RecruitDto
import net.myspring.basic.modules.hr.dto.RecruitReportDto
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
    fun findReportFilter(recruitQuery: RecruitQuery): MutableList<RecruitReportDto>
}

class RecruitRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): RecruitRepositoryCustom{
    override fun findPage(pageable: Pageable, recruitQuery: RecruitQuery): Page<RecruitDto> {
        var sb = StringBuilder("""
                select t1.*
                from hr_recruit t1
                where t1.enabled=1
        """);
        if(StringUtils.isNotBlank(recruitQuery.name)){
            sb.append("""
                   AND t1.name LIKE CONCAT ('%',:name,'%')
                """);
        }
        if(StringUtils.isNotBlank(recruitQuery.mobilePhone)){
            sb.append("""
                   AND t1.mobile_phone LIKE CONCAT ('%',:mobilePhone,'%')
                """);
        }
        if(recruitQuery.onJob!=null){
            sb.append("""
                   AND t1.on_job =:onJob
                """);
        }
        if(StringUtils.isNotBlank(recruitQuery.registerBy)){
            sb.append("""
                   AND t1.register_by LIKE CONCAT ('%',:registerBy,'%')
                """);
        }
        if(StringUtils.isNotBlank(recruitQuery.firstAppointBy)){
            sb.append("""
                   AND t1.first_appoint_by = :firstAppointBy
                """);
        }
        if(StringUtils.isNotBlank(recruitQuery.secondAppointBy)){
            sb.append("""
                   AND t1.second_appoint_by = :secondAppointBy
                """);
        }
        if(recruitQuery.firstAppointDateStart!=null){
            sb.append("""
                   AND t1.first_appoint_date >= :firstAppointDateStart
                """);
        }
        if(recruitQuery.firstAppointDateEnd!=null){
            sb.append("""
                   AND t1.first_appoint_date < :firstAppointDateEnd
                """);
        }
        if(recruitQuery.secondAppointDateStart!=null){
            sb.append("""
                   AND t1.second_appoint_date >= :secondAppointDateStart
                """);
        }
        if(recruitQuery.secondAppointDateEnd!=null){
            sb.append("""
                   AND t1.second_appoint_date < :secondAppointDateEnd
                """);
        }
        if(recruitQuery.auditAppointDateStart!=null){
            sb.append("""
                   AND t1.audit_appoint_date >= :auditAppointDateStart
                """);
        }
        if(recruitQuery.auditAppointDateEnd!=null){
            sb.append("""
                   AND t1.audit_appoint_date < :auditAppointDateEnd
                """);
        }
        if(recruitQuery.entryAppointDateStart!=null){
            sb.append("""
                   AND t1.entry_appoint_date >= :entryAppointDateStart
                """);
        }
        if(recruitQuery.entryAppointDateEnd!=null){
            sb.append("""
                   AND t1.entry_appoint_date >= :entryAppointDateEnd
                """);
        }
        if(recruitQuery.inviteDateStart!=null){
            sb.append("""
                   AND t1.invite_date >= :inviteDateStart
                """);
        }
        if(recruitQuery.inviteDateEnd!=null){
            sb.append("""
                   AND t1.invite_date >= :inviteDateEnd
                """);
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(recruitQuery), BeanPropertyRowMapper(RecruitDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(recruitQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

    override fun findReportFilter(recruitQuery: RecruitQuery): MutableList<RecruitReportDto> {
        var sb = StringBuilder("""
SELECT
    t.invite_date as exportTime,
	t.apply_position_name as applyPositionName,
	SUM( IF(t.invite_date,1,0)) as toRegisterCount,
  SUM( IF(t.first_appoint,1,0))  as  firstCount,
	SUM( IF(t.second_appoint,1,0)) as secondCount,
  SUM( IF(t.audit_appoint_date,1,0)) as  hireCount,
  SUM( IF(t.audit_appoint,1,0))  as auditCount,
	SUM( IF(t.entry_appoint='入职',1,0)) as onjobCount,

 IF(SUM( IF(t.first_appoint,1,0))/SUM( IF(t.invite_date,1,0)),SUM( IF(t.first_appoint,1,0))/SUM( IF(t.invite_date,1,0)),0) as registerRate,
 IF(SUM( IF(t.audit_appoint_date,1,0))/ SUM( IF(t.first_appoint,1,0)),SUM( IF(t.audit_appoint_date,1,0))/ SUM( IF(t.first_appoint,1,0)),0) as hireRate,
 IF(SUM( IF(t.entry_appoint='入职',1,0))/SUM( IF(t.audit_appoint_date,1,0)),SUM( IF(t.entry_appoint='入职',1,0))/SUM( IF(t.audit_appoint_date,1,0)),0)  as onjobRate,

	GROUP_CONCAT(CASE TRIM(t.entry_appoint)='入职' WHEN 1 THEN t. NAME WHEN 0 THEN null END) as onjob,
	GROUP_CONCAT(CASE TRIM(t.entry_appoint)='待定' WHEN 1 THEN	t. NAME	WHEN 0 THEN		null END) as afterOnjob,
	GROUP_CONCAT(CASE TRIM(t.entry_appoint)='放弃' OR TRIM(t.entry_appoint)='淘汰' WHEN 1 THEN	t. NAME	WHEN 0 THEN	null END) as abandon
FROM
	hr_recruit t
GROUP BY
	t.apply_position_name
                 """);
        if(recruitQuery.inviteDateStart!=null){
            sb.append("""
                   AND t1.invite_date >= :inviteDateStart
                """);
        }
        if(recruitQuery.inviteDateEnd!=null){
            sb.append("""
                   AND t1.invite_date >= :inviteDateEnd
                """);
        }

        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(recruitQuery),BeanPropertyRowMapper(RecruitReportDto::class.java))
    }

}
