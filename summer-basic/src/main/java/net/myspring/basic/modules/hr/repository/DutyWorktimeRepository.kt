package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyWorktime
import net.myspring.basic.modules.hr.dto.DutyWorktimeDto
import net.myspring.basic.modules.hr.web.query.DutyWorktimeQuery
import net.myspring.util.collection.CollectionUtil
import org.springframework.data.repository.query.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/24.
 */
interface DutyWorktimeRepository : BaseRepository<DutyWorktime,String>,DutyWorktimeRepositoryCustom{

    @Query("""
        SELECT
        t1
        FROM #{#entityName} t1
        WHERE
        t1.enabled=1
        AND t1.dutyDate >= :dateStart
        and t1.dutyDate <= :dateEnd
    """)
    fun findByDutyDate(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): MutableList<DutyWorktime>

    @Query("""
        SELECT
        t1
        FROM #{#entityName} t1
        WHERE
        t1.enabled=1
        and t1.employeeId=:employeeId
        AND t1.dutyDate >= :dateStart
        and t1.dutyDate <= :dateEnd
    """)
    fun findByEmployeeAndDate(@Param("employeeId") employeeId: String, @Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): MutableList<DutyWorktime>


}
interface DutyWorktimeRepositoryCustom{
    fun findByAccountIdAndDutyDate(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("accountIds") accountIds: MutableList<Long>): MutableList<DutyWorktime>

    fun findPage(pageable: Pageable, dutyWorktimeQuery: DutyWorktimeQuery): Page<DutyWorktimeDto>
}
class DutyWorktimeRepositoryImpl  @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DutyWorktimeRepositoryCustom{
    override fun findPage(pageable: Pageable, dutyWorktimeQuery: DutyWorktimeQuery): Page<DutyWorktimeDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyWorktime> {
        var sb = StringBuilder()
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", dateStart)
        paramMap.put("dateEnd", dateEnd)
        paramMap.put("accountIds", accountIds)
        sb.append("""
            SELECT
            w.employee_id,
            w.duty_date,
            if(min(w.duty_time) <= '12:00:00',min(w.duty_time),'') as dutyTimeStart,
            if(max(w.duty_time) >='12:00:00',max(w.duty_time),'') as dutyTimeEnd
            FROM
            hr_duty_worktime w
            WHERE
            w.duty_date >= :dateStart
            and  w.duty_date <= :dateEnd
        """)
        if (CollectionUtil.isNotEmpty(accountIds)) {
            sb.append(" and w.employee_id in (:accountIds)")
        }
        sb.append("""
            GROUP BY
            w.employee_id,
            w.duty_date ASC
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(DutyWorktime::class.java))

    }

}