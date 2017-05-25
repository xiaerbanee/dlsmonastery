package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyPublicFree
import net.myspring.basic.modules.hr.dto.DutyDto
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by lihx on 2017/5/24.
 */
interface DutyPublicFreeRepository : BaseRepository<DutyPublicFree,String>{
    @Query("""
        SELECT
        '公休' as dutyType,t1.free_date AS dutyDate,t1.remarks,t2.login_name as 'account.loginName',t2.leader_id AS 'account.leaderId' ,'GX' AS 'prefix',t1.id
        FROM
        hr_duty_public_free t1 , hr_account t2 ,hr_employee t3
        WHERE
        t1.enabled=1 AND t1.employee_id=t3.id and t3.account_id=t2.id
        AND t2.leader_id=?1 AND t1.status=?2 AND t1.created_date>=?3
    """, nativeQuery = true)
    fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): List<DutyDto>

    @Query("""
        SELECT
        t1.*
        FROM
        hr_duty_public_free t1
        WHERE
        t1.enabled=1
        AND t1.free_date >= ?1
        and t1.free_date <= ?2
        and t1.status in ?3
    """, nativeQuery = true)
    fun findByDateAndStatusList(dateStart: LocalDate, dateEnd: LocalDate, statusList: List<String>): List<DutyPublicFree>

    @Query("""
        SELECT
        t1.*
        FROM
        hr_duty_public_free t1
        WHERE
        t1.enabled=1
        and t1.employee_id=?1
        and t1.free_date >= ?2
        and t1.free_date <= ?3
    """, nativeQuery = true)
    fun findByEmployeeAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate): List<DutyPublicFree>
}