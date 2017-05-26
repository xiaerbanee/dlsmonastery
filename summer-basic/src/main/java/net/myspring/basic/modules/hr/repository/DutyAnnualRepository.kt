package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyAnnual
import net.myspring.basic.modules.hr.dto.DutyAnnualDto
import net.myspring.basic.modules.hr.web.query.DutyAnnualQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/24.
 */
interface DutyAnnualRepository : BaseRepository<DutyAnnual,String>,DutyAnnualRepositoryCustom{
    @Query("""
        select t
        FROM  #{#entityName} t
        where t.employeeId=?1
        order by annualYear desc
    """)
    fun findByEmployee(employeeId: String): List<DutyAnnual>
}
interface DutyAnnualRepositoryCustom{
    fun findPage(pageable: Pageable, dutyAnnualQuery: DutyAnnualQuery): Page<DutyAnnualDto>
}
class DutyAnnualRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DutyAnnualRepositoryCustom{
    override fun findPage(pageable: Pageable, dutyAnnualQuery: DutyAnnualQuery): Page<DutyAnnualDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}