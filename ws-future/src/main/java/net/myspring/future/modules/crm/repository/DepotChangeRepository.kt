package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.*

import net.myspring.future.modules.crm.dto.ExpressOrderDto
import org.apache.ibatis.annotations.Param
import org.springframework.data.jpa.repository.Query


interface DepotChangeRepository : BaseRepository<DepotChange, String> {





}
