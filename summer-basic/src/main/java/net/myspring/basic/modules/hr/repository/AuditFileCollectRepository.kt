package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AuditFileCollect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Created by lihx on 2017/5/24.
 */
interface AuditFileCollectRepository : BaseRepository<AuditFileCollect, String>, AuditFileCollectRepositoryCustom {
    fun findByAccountIdAndAuditFileIdAndEnabledIsTrue(accountId: String, auditFileId: String): AuditFileCollect
}

interface AuditFileCollectRepositoryCustom {

}

class AuditFileCollectRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : AuditFileCollectRepositoryCustom {
}
