package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BasAssistant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by lihx on 2017/6/8.
 */
@Component
class BasAssistantRepository  @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findByNameList(nameList: List<String>): MutableList<BasAssistant> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t2.FENTRYID,
                t2.FNUMBER,
                t1.FDATAVALUE,
                t2.FDOCUMENTSTATUS,
                t2.FFORBIDSTATUS
            FROM
                T_BAS_ASSISTANTDATAENTRY_L t1,
                T_BAS_ASSISTANTDATAENTRY t2,
                T_BAS_ASSISTANTDATA_L t3
            WHERE
                t2.FENTRYID = t1.FENTRYID
                AND t2.FID = t3.FID
                AND t1.FDATAVALUE in (:nameList)
        """, Collections.singletonMap("nameList", nameList), BeanPropertyRowMapper(BasAssistant::class.java))
    }

    fun findByType(type: String): MutableList<BasAssistant> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t2.FENTRYID,
                t2.FNUMBER,
                t1.FDATAVALUE,
                t2.FDOCUMENTSTATUS,
                t2.FFORBIDSTATUS
            FROM
                T_BAS_ASSISTANTDATAENTRY_L t1,
                T_BAS_ASSISTANTDATAENTRY t2,
                T_BAS_ASSISTANTDATA_L t3
            WHERE
                t2.FENTRYID = t1.FENTRYID
                AND t2.FID = t3.FID
                AND t3.FNAME = :type
        """, Collections.singletonMap("type", type), BeanPropertyRowMapper(BasAssistant::class.java))
    }
}