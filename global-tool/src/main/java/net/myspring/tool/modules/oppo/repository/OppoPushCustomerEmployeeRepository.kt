package net.myspring.tool.modules.oppo.repository

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPushCustomerEmployee
import org.springframework.data.jpa.repository.Query


interface OppoPushCustomerEmployeeRepository :BaseRepository<OppoPushCustomerEmployee,String>{
    @Query("select t1 from  #{#entityName} t1 where t1.companyName = ?1")
    fun findByCompanyName(companyName:String):MutableList<OppoPushCustomerEmployee>
}