package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutySign
import net.myspring.basic.modules.hr.dto.DutyDto
import net.myspring.basic.modules.hr.dto.DutySignDto
import net.myspring.basic.modules.hr.web.query.DutySignQuery
import net.myspring.util.collection.CollectionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by lihx on 2017/5/24.
 */
interface DutySignRepository : BaseRepository<DutySign,String>,DutySignRepositoryCustom{
    @Query("""
        SELECT
        t1
        FROM #{#entityName} t1
        WHERE
        t1.enabled=1
        and t1.employeeId=?1
        AND t1.dutyDate >= ?2
        and t1.dutyDate <=?3
    """)
    fun findByEmployeeAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate): MutableList<DutySign>
}
interface DutySignRepositoryCustom{
    fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): MutableList<DutyDto>

    fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutySign>

    fun findPage(pageable: Pageable, dutySignQuery: DutySignQuery): Page<DutySignDto>

    fun findByFilter(dutySignQuery: DutySignQuery): MutableList<DutySign>
}
class DutySignRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DutySignRepositoryCustom{
    override fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): MutableList<DutyDto> {
        var paramMap = HashMap<String, Any>()
        paramMap.put("leaderId", leaderId)
        paramMap.put("status", status)
        paramMap.put("dateStart", dateStart)
        return namedParameterJdbcTemplate.query("""
            SELECT
            '签到' as dutyType,t1.duty_date,t1.remarks,t2.login_name as 'account.loginName',t2.leader_id AS 'account.leaderId' ,'QD' AS 'prefix',t1.id
            FROM
            hr_duty_sign t1 , hr_account t2 ,hr_employee t3
            WHERE
            t1.enabled=1 AND t1.employee_id=t3.id and t3.account_id=t2.id
            AND t2.leader_id=:leaderId AND t1.status=:status AND t1.created_date>=:dateStart
        """, paramMap, BeanPropertyRowMapper(DutyDto::class.java))
    }

    override fun findByFilter(dutySignQuery: DutySignQuery): MutableList<DutySign> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findPage(pageable: Pageable, dutySignQuery: DutySignQuery): Page<DutySignDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutySign> {
        var sb = StringBuilder()
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", dateStart)
        paramMap.put("dateEnd", dateEnd)
        paramMap.put("accountIds", accountIds)
        sb.append("""
            SELECT
            ds.employee_id,
            ds.duty_date,
            ds.duty_time
            FROM
            hr_duty_sign ds
            WHERE
            ds.duty_date >= :dateStart
            AND ds.duty_date <= :dateEnd
            and ds.enabled=1
        """)
        if (CollectionUtil.isNotEmpty(accountIds)) {
            sb.append(" and ds.employee_id in (:accountIds)")
        }
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap,BeanPropertyRowMapper(DutySign::class.java))
    }

}