package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.salary.domain.SalaryTemplateDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Created by lihx on 2017/5/25.
 */
interface SalaryTemplateDetailRepository : BaseRepository<SalaryTemplateDetail,String>,SalaryTemplateDetailRepositoryCustom{
    fun findBySalaryTemplateId(salaryTemplateId:String):MutableList<SalaryTemplateDetail>
}
interface SalaryTemplateDetailRepositoryCustom{
}
class SalaryTemplateDetailRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): SalaryTemplateDetailRepositoryCustom{

}