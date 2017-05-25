package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyFree
import net.myspring.basic.modules.hr.dto.DutyDto
import net.myspring.basic.modules.hr.dto.DutyFreeDto
import net.myspring.basic.modules.hr.web.query.DutyFreeQuery
import net.myspring.util.collection.CollectionUtil
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/24.
 */
interface DutyFreeRepository : BaseRepository<DutyFree, String>, DutyFreeRepositoryCustom {

    @Query("""
        SELECT
            t1.*
            FROM
            hr_duty_free t1
            WHERE
             t1.enabled=1
            AND  t1.free_date=?1
            and t1.employee_id=?2
    """, nativeQuery = true)
    fun findByDate(freeDate: LocalDate, employeeId: String): List<DutyFree>

    @Query("""
        SELECT
            '免打卡' as dutyType,
            t1.free_date AS dutyDate,
            t1.remarks,
            t2.login_name as 'account.loginName',
            t2.leader_id AS 'account.leaderId' ,
            'MDK' AS 'prefix',
            t1.id
            FROM
            hr_duty_free t1 ,
            hr_account t2 ,
            hr_employee t3
            WHERE
            t1.enabled=1
            AND t1.employee_id=t3.id
            and t3.account_id=t2.id
            AND t2.leader_id=?1
            AND t1.status=?2
            AND t1.created_date>=?3
    """, nativeQuery = true)
    fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): List<DutyDto>

    @Query("""
        SELECT
            t1.*
            FROM
            hr_duty_free t1
            WHERE
            t1.enabled=1
            AND t1.free_date >= ?1
            and t1.free_date <= ?2
            and t1.status in ?3
        """, nativeQuery = true)
    fun findByDateAndStatusList(dateStart: LocalDate, dateEnd: LocalDate, statusList: List<String>): List<DutyFree>

    @Query("""
        SELECT
            t1.*
            FROM
            hr_duty_free t1
            WHERE
            t1.enabled=1
            and t1.employee_id=?1
            and t1.free_date >=?2
            and t1.free_date &lt;=?3
    """, nativeQuery = true)
    fun findByEmployeeAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate): List<DutyFree>
}

interface DutyFreeRepositoryCustom {
    fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: List<Long>): List<DutyFree>

    fun findPage(pageable: Pageable, dutyFreeQuery: DutyFreeQuery): Page<DutyFreeDto>
}

class DutyFreeRepositoryImpl @Autowired constructor(val entityManager: EntityManager) : DutyFreeRepositoryCustom {
    override fun findPage(pageable: Pageable, dutyFreeQuery: DutyFreeQuery): Page<DutyFreeDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: List<Long>): List<DutyFree> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            fr.employee_id,
            fr.free_date as 'duty_date',
            fr.date_type
            FROM
            hr_duty_free fr
            WHERE
            fr.free_date >= :dateStart
            AND fr.free_date <= :dateEnd
        """);
        if (CollectionUtil.isNotEmpty(accountIds)) {
            sb.append(" and fr.employee_id in :accountIds")
        }

        var query = entityManager.createNativeQuery(sb.toString(), DutyFree::class.java);
        query.setParameter("dateStart", dateStart);
        query.setParameter("dateEnd", dateEnd);
        query.setParameter("accountIds", accountIds);
        return query.resultList as List<DutyFree>

    }

}