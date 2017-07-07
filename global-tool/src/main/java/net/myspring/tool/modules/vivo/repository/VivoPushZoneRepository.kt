package net.myspring.tool.modules.vivo.repository

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPushZones
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface VivoPushZoneRepository : BaseRepository<VivoPushZones, String>, VivoPushZoneRepositoryCustom {

}
interface VivoPushZoneRepositoryCustom{

}
class VivoPushZoneRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : VivoPushZoneRepositoryCustom{

}
