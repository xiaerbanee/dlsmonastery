package net.myspring.general.modules.sys.repository

import com.google.common.collect.Maps
import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.FolderFile
import net.myspring.general.modules.sys.domain.ProcessType
import net.myspring.general.modules.sys.dto.ProcessTaskDto
import net.myspring.general.modules.sys.dto.ProcessTypeDto
import net.myspring.general.modules.sys.web.query.FolderFileQuery
import net.myspring.general.modules.sys.web.query.ProcessTypeQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

interface ProcessTypeRepository : BaseRepository<ProcessType, String>,ProcessTypeRepositoryCustom {

    fun findByName(name: String): ProcessType

    fun findByAuditFileTypeIsTrueAndEnabledIsTrue():MutableList<ProcessType>


    @Query("select t from #{#entityName} t where t.auditFileType=1 and t.enabled=1")
    fun findEnabledAuditFileType(): MutableList<ProcessType>


}

interface ProcessTypeRepositoryCustom {
    fun findPage(pageable: Pageable, processTypeQuery: ProcessTypeQuery): Page<ProcessTypeDto>;

    fun findByCreatePositionIdsLike(positionIdList:MutableList<String>):MutableList<ProcessType>

    fun findByViewPositionIdsLike(positionIdList: MutableList<String>):MutableList<ProcessType>

}

class ProcessTypeRepositoryImpl@Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ProcessTypeRepositoryCustom {
    override fun findByCreatePositionIdsLike(positionIdList:MutableList<String>): MutableList<ProcessType> {
        var sb = StringBuilder()
        sb.append("""
                SELECT
                t1.*
            FROM
            sys_process_type t1
                WHERE
            t1.audit_file_type=1
            and (
        """)
        for ((index) in positionIdList.withIndex()) {
            sb.append("  t1.create_position_ids like :positionId").append(index);
            if (index < positionIdList.size - 1) {
                sb.append(" or ");
            }
        }
        sb.append(")");
        var paramMap = Maps.newHashMap<String, String>();
        for ((index, value) in positionIdList.withIndex()) {
            paramMap.put("positionId" + index, "%,$value,%");
        }
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(ProcessType::class.java));
    }

    override fun findByViewPositionIdsLike(positionIdList: MutableList<String>): MutableList<ProcessType> {
        var sb = StringBuilder()
        sb.append("""
                SELECT
                t1.*
            FROM
            sys_process_type t1
                WHERE
         t1.audit_file_type=1
            and (
        """)
        for ((index) in positionIdList.withIndex()) {
            sb.append("  t1.view_position_ids like :positionId").append(index);
            if (index < positionIdList.size - 1) {
                sb.append(" or ");
            }
        }
        sb.append(")");
        var paramMap = Maps.newHashMap<String, String>();
        for ((index, value) in positionIdList.withIndex()) {
            paramMap.put("positionId" + index, "%,$value,%");
        }
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(ProcessType::class.java));
    }

    override fun findPage(pageable: Pageable, processTypeQuery: ProcessTypeQuery): Page<ProcessTypeDto> {

        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*
            FROM
            sys_process_type t1
                WHERE
            t1.enabled=1
        """)

        if (StringUtils.isNotBlank(processTypeQuery.name)) {
            sb.append("""
                AND t1.name LIKE CONCAT('%',:name,'%')
            """)
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(processTypeQuery), BeanPropertyRowMapper(ProcessTypeDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(processTypeQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}