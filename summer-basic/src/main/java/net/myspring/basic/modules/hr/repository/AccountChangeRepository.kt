package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AccountChange
import net.myspring.basic.modules.hr.web.form.AccountChangeForm
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery
import org.apache.ibatis.annotations.Param

interface AccountChangeRepository : BaseRepository<AccountChange, String> {

    fun getForm(@Param("p") accountChangeQuery: AccountChangeQuery): AccountChangeForm
}