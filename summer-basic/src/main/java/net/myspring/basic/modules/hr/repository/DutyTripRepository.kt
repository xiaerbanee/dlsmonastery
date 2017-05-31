package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyTrip
import net.myspring.basic.modules.hr.dto.DutyDto
import net.myspring.basic.modules.hr.dto.DutyTripDto
import net.myspring.basic.modules.hr.dto.DutyWorktimeDto
import net.myspring.basic.modules.hr.web.query.DutyTripQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * Created by lihx on 2017/5/24.
 */

interface DutyTripRepository : BaseRepository<DutyTrip, String>,DutyTripRepositoryCustom {

    @Query("""
        SELECT
        t1
        FROM #{#entityName} t1
        WHERE
        t1.enabled=1
        and t1.employeeId= :employeeId
        and ((t1.dateStart >= :dateStart
        and t1.dateStart <= :dateEnd)
        or  (t1.dateEnd >= :dateStart
        and t1.dateEnd <= :dateEnd))
    """)
    fun findByEmployeeAndDate(@Param("employeeId") employeeId: String, @Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): MutableList<DutyTrip>

    @Query("""
        SELECT
        t1
        FROM #{#entityName} t1
        WHERE
        t1.enabled=1
        AND t1.dateStart >= :dateStart
        and t1.dateEnd <= :dateEnd
        and t1.status in :statusList
    """)
    fun findByDateAndStatusList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("statusList") statusList: MutableList<String>): MutableList<DutyTrip>


}
interface DutyTripRepositoryCustom{
    fun findByAuditable(@Param("leaderId") leaderId: String, @Param("status") status: String, @Param("createdDateStart") createdDateStart: LocalDateTime): MutableList<DutyDto>

    fun findByAccountIdAndDutyDate(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("accountIds") accountIds: MutableList<Long>): MutableList<DutyTrip>

    fun findPage(pageable: Pageable, dutyTripQuery: DutyTripQuery): Page<DutyTripDto>
}
class DutyTripRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DutyTripRepositoryCustom{
    override fun findByAuditable(leaderId: String, status: String, createdDateStart: LocalDateTime): MutableList<DutyDto> {
        var paramMap = HashMap<String, Any>()
        paramMap.put("leaderId", leaderId)
        paramMap.put("status", status)
        paramMap.put("createdDateStart", createdDateStart)
        return namedParameterJdbcTemplate.query("""
            SELECT
            '出差' as dutyType,
            CONCAT(t1.date_start,'~',t1.date_end) as dutyDate,
            t1.remarks,
            t2.login_name as 'account.loginName',
            t2.leader_id AS 'account.leaderId' ,
            'CC' AS 'prefix',
            t1.id
            FROM
            hr_duty_trip t1 , hr_account t2 ,hr_employee t3
            WHERE
              t1.enabled=1
              AND t1.employee_id=t3.id
              and t3.account_id=t2.id
              AND t2.leader_id=:leaderId
              AND t1.status=:status
              AND t1.created_date>=:createdDateStart
        """, paramMap, BeanPropertyRowMapper(DutyDto::class.java))
    }

    override fun findPage(pageable: Pageable, dutyTripQuery: DutyTripQuery): Page<DutyTripDto> {
        var sql = StringBuilder("""
                SELECT
                t1.*
                FROM
                hr_duty_trip t1,
                hr_account account,
                sys_office office
                WHERE
                t1.created_by=account.id
                and account.office_id=office.id
                and t1.enabled=1
            """);
        if(dutyTripQuery.dutyDateStart!=null){
            sql.append("""
                   AND t1.duty_date > :dutyDateStart
                """);
        }
        if(dutyTripQuery.dutyDateEnd!=null){
            sql.append("""
                   AND t1.duty_date &lt; :dutyDateEnd
                """);
        }
        if(dutyTripQuery.createdBy!=null){
            sql.append("""
                AND t1.created_by=:createdBy
                """);
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sql.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sql.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(dutyTripQuery), BeanPropertyRowMapper(DutyTripDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(dutyTripQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }

    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyTrip> {
        var sb = StringBuilder()
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", dateStart)
        paramMap.put("dateEnd", dateEnd)
        paramMap.put("accountIds", accountIds)
        sb.append("""
            SELECT
            tr.employee_id,tr.date_start,tr.date_end,tr.status
            FROM
            hr_duty_trip
            WHERE
            (
            date_end >= :dateStart
            and date_end <= :dateEnd
            )
            or (date_start >= :dateStart and date_start <= :dateEnd)
            or (date_start < :dateStart and date_end > :dateStart)
            FROM
            hr_duty_trip tr
            WHERE
            tr.enabled=1
        """)
        if (CollectionUtil.isNotEmpty(accountIds)) {
            sb.append(" and tr.employee_id in (:accountIds)")
        }
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(DutyTrip::class.java))

    }

}