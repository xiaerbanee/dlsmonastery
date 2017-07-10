package net.myspring.tool.modules.vivo.repository

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPushSCustomers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

interface VivoPushSCustomersRepository :BaseRepository<VivoPushSCustomers,String>,VivoPushSCustomersRepositoryCustom{

}

interface VivoPushSCustomersRepositoryCustom{

}

class VivoPushScustomersRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :VivoPushSCustomersRepositoryCustom{

}