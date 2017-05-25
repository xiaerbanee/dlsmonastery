package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyTrip
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager
import net.myspring.basic.modules.hr.dto.DutyDto
import net.myspring.basic.modules.hr.dto.DutyTripDto
import net.myspring.basic.modules.hr.web.query.DutyTripQuery
import net.myspring.util.collection.CollectionUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * Created by lihx on 2017/5/24.
 */

interface DutyTripRepository : BaseRepository<DutyTrip, String>,DutyTripRepositoryCustom {
    @Query("""
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
          AND t2.leader_id=:leader
          AND t1.status=:status
          AND t1.created_date>=:createdDateStart
    """, nativeQuery = true)
    fun findByAuditable(@Param("leaderId") leaderId: String, @Param("status") status: String, @Param("createdDateStart") createdDateStart: LocalDateTime): MutableList<DutyDto>

    @Query("""
        SELECT
        t1.*
        FROM
        hr_duty_trip t1
        WHERE
        t1.enabled=1
        and t1.employee_id=#{employeeId}
        and ((t1.date_start >= :dateStart
        and t1.date_start &lt;= :dateEnd)
        or  (t1.date_end >= :dateStart
        and t1.date_end &lt;= :dateEnd))
    """, nativeQuery = true)
    fun findByEmployeeAndDate(@Param("employeeId") employeeId: String, @Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): MutableList<DutyTrip>

    @Query("""
        SELECT
        t1.*
        FROM
        hr_duty_trip t1
        WHERE
        t1.enabled=1
        AND t1.date_start >= :dateStart
        and t1.date_end &lt;= :dateEnd
        and t1.status in :statusList
    """, nativeQuery = true)
    fun findByDateAndStatusList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("statusList") statusList: MutableList<String>): MutableList<DutyTrip>


}
interface DutyTripRepositoryCustom{
    fun findByAccountIdAndDutyDate(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("accountIds") accountIds: MutableList<Long>): MutableList<DutyTrip>

    fun findPage(pageable: Pageable, dutyTripQuery: DutyTripQuery): Page<DutyTripDto>
}
class DutyTripRepositoryImpl @Autowired constructor(val entityManager: EntityManager): DutyTripRepositoryCustom{
    override fun findPage(pageable: Pageable, dutyTripQuery: DutyTripQuery): Page<DutyTripDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyTrip> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            tr.employee_id,tr.date_start,tr.date_end,tr.status
            FROM
            hr_duty_trip
            WHERE
            (
            date_end &gt;= :dateStart
            and date_end &lt;= :dateEnd
            )
            or (date_start &gt;= :dateStart and date_start &lt;= :dateEnd)
            or (date_start &lt; :dateStart and date_end &gt; :dateStart)
            FROM
            hr_duty_trip tr
            WHERE
            tr.enabled=1
        """)
        if (CollectionUtil.isNotEmpty(accountIds)) {
            sb.append(" and tr.employee_id in :accountIds")
        }
        var query = entityManager.createNativeQuery(sb.toString(), DutyTrip::class.java)
        query.setParameter("dateStart", dateStart)
        query.setParameter("dateEnd", dateEnd)
        query.setParameter("accountIds", accountIds)
        return query.resultList as MutableList<DutyTrip>
    }

}