package net.myspring.report.modules.future.repository

import net.myspring.report.modules.future.dto.DepotDto
import net.myspring.report.modules.future.web.query.DepotQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class DepotRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findByAccountId(accountId: String): MutableList<DepotDto> {
        return namedParameterJdbcTemplate.query("""
        SELECT
            t1.*
        FROM
            crm_depot t1,
            crm_account_depot t2
        WHERE
            t1.enabled = 1
        AND t1.is_hidden = 0
        AND t1.id = t2.depot_id
        AND t2.account_id = :accountId
          """, Collections.singletonMap("accountId", accountId), BeanPropertyRowMapper(DepotDto::class.java))
    }

     fun findFilter(depotQuery: DepotQuery): MutableList<DepotDto> {
        val sb = StringBuffer("""
            SELECT
                t1.*
            FROM
                crm_depot t1
            WHERE
             t1.enabled = 1
        """)
        if(StringUtils.isNotBlank(depotQuery.name)){
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        if(StringUtils.isNotBlank(depotQuery.areaId)){
            sb.append("""  and t1.office_id in (:officeIds) """)
        }
        if(StringUtils.isNotBlank(depotQuery.officeId)){
            sb.append("""  and t1.office_id=:officeId """)
        }
        if(CollectionUtil.isNotEmpty(depotQuery.depotIdList)){
            sb.append("""  and t1.id in (:depotIdList) """)
        }
        if(CollectionUtil.isNotEmpty(depotQuery.officeIdList)){
            sb.append("""  and t1.office_id in (:officeIdList) """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(depotQuery), BeanPropertyRowMapper(DepotDto::class.java))
    }

}