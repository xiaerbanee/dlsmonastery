package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AccountChange
import net.myspring.basic.modules.hr.dto.AccountChangeDto
import net.myspring.basic.modules.hr.web.form.AccountChangeForm
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

interface AccountChangeRepository : BaseRepository<AccountChange, String>,AccountChangeRepositoryCustom {

}

interface AccountChangeRepositoryCustom {
    fun getForm(accountChangeQuery: AccountChangeQuery): AccountChangeForm

    fun findPage(pageable:Pageable,accountChangeQuery:AccountChangeQuery): Page<AccountChangeDto>
}


class AccountChangeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AccountChangeRepositoryCustom {
    override fun findPage(pageable: Pageable, accountChangeQuery: AccountChangeQuery): Page<AccountChangeDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getForm(accountChangeQuery: AccountChangeQuery): AccountChangeForm {
        var sb = StringBuilder();
        sb.append( """
            SELECT
                t2.id AS 'accountId',
                t2.office_id,
                t2.leader_id,
                t2.position_id,
                t3.mobile_phone AS 'employee.mobilePhone',
                t3.idcard AS 'employee.idcard',
                t3.entry_date AS 'employee.entryDate',
                t3.bank_number AS 'employee.bankNumber',
                t3.salary AS 'employee.salary',
                t3.regular_date AS 'employee.regularDate',
                t3.leave_date AS 'employee.leaveDate'
            FROM
                hr_account t2,
                hr_employee t3
            WHERE
                t2.employee_id = t3.id
       """);
        if(StringUtils.isNotBlank(accountChangeQuery.id)) {
            sb.append(" and t2.id=(SELECT account_id FROM hr_account_change where id=:id)");
        }
        if(StringUtils.isNotBlank(accountChangeQuery.accountId)) {
            sb.append(" and t2.id=:accountId");
        }
        return namedParameterJdbcTemplate.queryForObject(sb.toString(), BeanPropertySqlParameterSource(accountChangeQuery), BeanPropertyRowMapper(AccountChangeForm::class.java))

    }
}