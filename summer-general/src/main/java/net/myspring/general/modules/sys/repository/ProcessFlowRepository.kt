package net.myspring.general.modules.sys.repository

import net.myspring.general.common.config.MyBeanPropertyRowMapper
import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.ProcessFlow
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface ProcessFlowRepository : BaseRepository<ProcessFlow, String>, ProcessFlowRepositoryCustom {

    fun findByProcessTypeIdAndName(processTypeId: String, name: String): ProcessFlow

    fun findByProcessTypeId(processTypeId: String): MutableList<ProcessFlow>

}


interface ProcessFlowRepositoryCustom{
    fun findByProcessTypeName(processTypeName: String): MutableList<ProcessFlow>
}

class ProcessFlowRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ProcessFlowRepositoryCustom {
    override fun findByProcessTypeName(processTypeName: String): MutableList<ProcessFlow> {
        return namedParameterJdbcTemplate.query("""
        SELECT
            t1.*
        FROM
            process_flow t1,
            Process_type t2
        WHERE
            t1.process_type_id = t2.id
        AND t2.enabled = 1
        AND t2.name =:processTypeName
          """, Collections.singletonMap("processTypeName", processTypeName), MyBeanPropertyRowMapper(ProcessFlow::class.java))
    }


}