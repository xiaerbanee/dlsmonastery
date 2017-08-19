package net.myspring.tool.modules.oppo.repository

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantDept
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

interface OppoPlantDeptRepository :BaseRepository<OppoPlantDept,String>,OppoPlantDeptRepositoryCustom{

}

interface OppoPlantDeptRepositoryCustom{

}

class OppoPlantDeptRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):OppoPlantDeptRepositoryCustom{

}