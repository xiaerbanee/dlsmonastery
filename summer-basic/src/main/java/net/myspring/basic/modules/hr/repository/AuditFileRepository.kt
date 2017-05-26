package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AuditFile
import net.myspring.basic.modules.hr.dto.AuditFileDto
import net.myspring.basic.modules.hr.web.query.AuditFileQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/24.
 */
interface AuditFileRepository : BaseRepository<AuditFile,String>,AuditFileRepositoryCustom{

}
interface AuditFileRepositoryCustom{
    fun findPage(pageable: Pageable, auditFileQuery: AuditFileQuery): Page<AuditFileDto>

}
class AuditFileRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :AuditFileRepositoryCustom{
    override fun findPage(pageable: Pageable, auditFileQuery: AuditFileQuery): Page<AuditFileDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}