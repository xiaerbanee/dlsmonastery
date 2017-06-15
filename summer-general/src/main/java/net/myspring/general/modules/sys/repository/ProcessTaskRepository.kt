package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.ProcessFlow
import net.myspring.general.modules.sys.domain.ProcessTask
import net.myspring.general.modules.sys.dto.ProcessTaskDto
import net.myspring.general.modules.sys.web.query.ProcessTaskQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*

interface ProcessTaskRepository : BaseRepository<ProcessTask, String>,ProcessTaskRepositoryCustom {

    fun findByProcessInstanceId(processInstanceId: String): ProcessTask

    fun findByPositionIdAndOfficeIdIn(positionId:String,officeId:MutableList<String>):MutableList<ProcessTask>
}

interface ProcessTaskRepositoryCustom{

    fun findPage(pageable: Pageable,processTaskQuery: ProcessTaskQuery): Page<ProcessTaskDto>

}

class ProcessTaskRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ProcessTaskRepositoryCustom {
    override fun findPage(pageable: Pageable, processTaskQuery: ProcessTaskQuery): Page<ProcessTaskDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*
            FROM
            sys_process_task t1
            WHERE
            t1.enabled=1
        """)

        if (CollectionUtil.isNotEmpty(processTaskQuery.officeIds)) {
            sb.append("""
                and t1.office_id in (:officeIds)
            """)
        }
        if (StringUtils.isNotBlank(processTaskQuery.positionId)) {
            sb.append("""
                and t1.position_id =:positionId
            """)
        }
        if (StringUtils.isNotBlank(processTaskQuery.name)) {
            sb.append("""
                and t1.name like concat('%',:name,'%')
            """)
        }
        if (StringUtils.isNotBlank(processTaskQuery.status)) {
            sb.append("""
                and t1.status like concat('%',:status,'%')
            """)
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(processTaskQuery),BeanPropertyRowMapper(ProcessTaskDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(processTaskQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}