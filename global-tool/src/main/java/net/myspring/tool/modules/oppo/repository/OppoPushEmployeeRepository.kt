package net.myspring.tool.modules.oppo.repository

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPushEmployee
import org.springframework.data.jpa.repository.Query

interface OppoPushEmployeeRepository :BaseRepository<OppoPushEmployee,String>{
    @Query("select t1 from  #{#entityName} t1 where t1.companyName = ?1 ")
    fun findByCompanyName(companyName:String):MutableList<OppoPushEmployee>
}