package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.EmployeeDeposit
import net.myspring.future.modules.crm.domain.ExpressOrder

import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import org.apache.ibatis.annotations.Param
import org.springframework.data.jpa.repository.Query


interface EmployeeDepositRepository : BaseRepository<EmployeeDeposit, String> {





}
