package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyLeave
import net.myspring.basic.modules.hr.dto.DutyDto
import net.myspring.basic.modules.hr.dto.DutyLeaveDto
import net.myspring.basic.modules.hr.web.query.DutyLeaveQuery
import net.myspring.util.collection.CollectionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/24.
 */
interface DutyLeaveRepository : BaseRepository<DutyLeave, String>, DutyLeaveRepositoryCustom{
    @Query("""
        SELECT
              t1.*
            FROM
            hr_duty_leave t1
            WHERE
            t1.enabled=1
            AND t1.employee_id=?1 AND t1.duty_date=?2 AND t1.date_type=?3
    """, nativeQuery = true)
    fun findByEmployeeAndDateAndDateType(employeeId: String, date: LocalDate, dateType: String): DutyLeave

    @Query("""
        SELECT
            '请假' as dutyType,t1.duty_date,t1.remarks,t2.login_name as 'accountName' ,'QJ' AS 'prefix',t1.id
            FROM
            hr_duty_leave t1, hr_account t2,hr_employee t3
            WHERE
            t1.enabled=1 AND t1.employee_id=t3.id and t3.account_id=t2.id
            AND t2.leader_id=?1 AND t1.status=?2 AND t1.created_date>=?3
    """, nativeQuery = true)
    fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): MutableList<DutyDto>

    @Query("""
        SELECT
        t1.*
        FROM
        hr_duty_leave t1
        WHERE
        t1.enabled=1
        AND t1.employee_id=?1
        AND t1.duty_date in ?2
    """, nativeQuery = true)
    fun findByDutyDateList(employeeId: String, dutyDateList: MutableList<LocalDate>): MutableList<DutyLeave>

    @Query("""
        SELECT
            t1.*
            FROM
            hr_duty_leave t1
            WHERE
            t1.enabled=1
            AND t1.employee_id=?1
            AND t1.duty_date =?2
    """, nativeQuery = true)
    fun findByDutyDate(employeeId: String, dutyDate: LocalDate): MutableList<DutyLeave>

    @Query("""
        SELECT
            t1.*
            FROM
            hr_duty_leave t1
            WHERE
            t1.enabled=1
            AND t1.duty_date >= ?1 and t1.duty_date <= ?2
            and t1.status in ?3
    """, nativeQuery = true)
    fun findByDateAndStatusList(dateStart: LocalDate, dateEnd: LocalDate, statusList: MutableList<String>): MutableList<DutyLeave>

    @Query("""
        SELECT
            t1.*
            FROM
            hr_duty_leave t1
            WHERE
            t1.enabled=1
            AND t1.employee_id=?1
            AND t1.duty_date >=?2
            and t1.duty_date <=?3
    """, nativeQuery = true)
    fun findByEmployeeAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate): MutableList<DutyLeave>



}
interface DutyLeaveRepositoryCustom{
    fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyLeave>

    fun findPage(pageable: Pageable, dutyLeaveQuery: DutyLeaveQuery): Page<DutyLeaveDto>
}

class DutyLeaveRepositoryImpl @Autowired constructor(val entityManager: EntityManager):DutyLeaveRepositoryCustom{
    override fun findPage(pageable: Pageable, dutyLeaveQuery: DutyLeaveQuery): Page<DutyLeaveDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyLeave> {
        var sb = StringBuilder();
        sb.append("""
            SELECT
            le.employee_id,
            le.duty_date,
            le.leave_type,
            le.date_type
            FROM
            hr_duty_leave le
            WHERE
            le.duty_date  >= :dateStart
            AND le.duty_date  <= :dateEnd
        """);
        if (CollectionUtil.isNotEmpty(accountIds)){
            sb.append(" and le.employee_id in :accountIds")
        }
        sb.append(" ORDER BY le.employee_id,le.duty_date asc");
        var query = entityManager.createNativeQuery(sb.toString(), DutyLeave::class.java);
        query.setParameter("dateStart", dateStart);
        query.setParameter("dateEnd", dateEnd);
        query.setParameter("accountIds", accountIds);
        return query.resultList as MutableList<DutyLeave>
    }

}