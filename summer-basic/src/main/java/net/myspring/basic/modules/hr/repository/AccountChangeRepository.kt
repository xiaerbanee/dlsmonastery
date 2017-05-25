package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AccountChange
import net.myspring.basic.modules.hr.web.form.AccountChangeForm
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery
import net.myspring.util.repository.QueryUtils
import net.myspring.util.text.StringUtils
import org.apache.ibatis.annotations.Param
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager

interface AccountChangeRepository : BaseRepository<AccountChange, String>,AccountChangeRepositoryCustom {

}

interface AccountChangeRepositoryCustom {
    fun getForm(accountChangeQuery: AccountChangeQuery): AccountChangeForm
}


class AccountChangeRepositoryImpl @Autowired constructor(val entityManager: EntityManager):AccountChangeRepositoryCustom {
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

        var query = entityManager.createNativeQuery(sb.toString(),AccountChangeForm::class.java);
        QueryUtils.setParameter(query,accountChangeQuery);
        return query.firstResult as AccountChangeForm

    }
}