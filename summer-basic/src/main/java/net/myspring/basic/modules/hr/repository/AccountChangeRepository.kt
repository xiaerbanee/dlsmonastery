package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AccountChange
import net.myspring.basic.modules.hr.dto.AccountChangeDto
import net.myspring.basic.modules.hr.web.form.AccountChangeForm
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.util.CollectionUtils
import java.util.*

interface AccountChangeRepository : BaseRepository<AccountChange, String>,AccountChangeRepositoryCustom {

}

interface AccountChangeRepositoryCustom {
    fun getForm(accountChangeQuery: AccountChangeQuery): AccountChangeForm

    fun findPage(pageable:Pageable,accountChangeQuery:AccountChangeQuery): Page<AccountChangeDto>
}


class AccountChangeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AccountChangeRepositoryCustom {

    override fun findPage(pageable: Pageable, accountChangeQuery: AccountChangeQuery): Page<AccountChangeDto> {
        var sb = StringBuilder()
        sb.append("""
            select
            t1.*,account.office_id
            from
            hr_account_change t1,hr_account account,sys_office office
            where
            t1.enabled=1
            and t1.account_id=account.id
            and account.office_id=office.id
        """)
        if (CollectionUtils.isEmpty(accountChangeQuery.officeIds)) {
            sb.append(" and office.id IN :officeIds ")
        }
        if (accountChangeQuery.createdDate != null) {
            sb.append(" AND t1.created_date> :createdDateStart ")
        }
        if (accountChangeQuery.createdDateEnd != null) {
            sb.append(" AND t1.created_date < :createdDateEnd ")
        }
        if (accountChangeQuery.type != null) {
            sb.append(" and t1.type = :type ")
        }
        if (accountChangeQuery.createdByName != null) {
            sb.append("""
                and t1.account_id in(
                select t2.id
                from hr_account t2
                where t2.login_name like concat('%',:createdByName,'%')
                and t2.enabled=1
                )
            """)
        }
        if (accountChangeQuery.officeId != null) {
            sb.append("""
                and t1.account_id  in(
                select t2.id
                from hr_account t2 ,sys_office office
                where  t2.enabled=1
                and t2.office_id=office.id
                and (
                    office.parent_ids like concat('%,',:officeId,',%')
                  or office.id=:officeId
                )
                )
            """)
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(), pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(accountChangeQuery), BeanPropertyRowMapper(AccountChangeDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(accountChangeQuery), Long::class.java)
        return PageImpl(list, pageable, count)
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