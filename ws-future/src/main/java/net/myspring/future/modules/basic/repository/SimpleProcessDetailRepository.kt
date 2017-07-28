package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.SimpleProcessDetail
import net.myspring.future.modules.basic.dto.SimpleProcessDetailDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface SimpleProcessDetailRepository : BaseRepository<SimpleProcessDetail, String>, SimpleProcessDetailRepositoryCustom{
}

interface SimpleProcessDetailRepositoryCustom{
    fun findDtoList(simpleProcessId:String):MutableList<SimpleProcessDetailDto>
}

class SimpleProcessDetailRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):SimpleProcessDetailRepositoryCustom{
    override fun findDtoList(simpleProcessId:String): MutableList<SimpleProcessDetailDto> {

        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.*
            FROM
                crm_simple_process_detail t1
            WHERE
                t1.enabled = 1
            AND t1.simple_process_id = :simpleProcessId
            ORDER BY t1.created_date
          """, Collections.singletonMap("simpleProcessId", simpleProcessId), BeanPropertyRowMapper(SimpleProcessDetailDto::class.java))
    }

}

