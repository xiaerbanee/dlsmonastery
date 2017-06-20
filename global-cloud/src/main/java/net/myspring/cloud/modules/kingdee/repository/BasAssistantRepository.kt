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
                t1.FENTRYID,
                t1.FNUMBER,
                t2.FDATAVALUE,
                t3.FNAME as FType,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            FROM
                T_BAS_ASSISTANTDATAENTRY t1,
                T_BAS_ASSISTANTDATAENTRY_L t2,
                T_BAS_ASSISTANTDATA_L t3
            WHERE
                t1.FENTRYID = t2.FENTRYID
                and t1.FID = t3.FID
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                AND t2.FDATAVALUE in (:nameList)
        """, Collections.singletonMap("nameList", nameList), BeanPropertyRowMapper(BasAssistant::class.java))
    }

    fun findByName(name: String): BasAssistant {
        return namedParameterJdbcTemplate.queryForObject("""
            SELECT
                t1.FENTRYID,
                t1.FNUMBER,
                t2.FDATAVALUE,
                t3.FNAME as FType,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            FROM
                T_BAS_ASSISTANTDATAENTRY t1,
                T_BAS_ASSISTANTDATAENTRY_L t2,
                T_BAS_ASSISTANTDATA_L t3
            WHERE
                t1.FENTRYID = t2.FENTRYID
                and t1.FID = t3.FID
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                AND t2.FDATAVALUE = :name
        """, Collections.singletonMap("name", name), BeanPropertyRowMapper(BasAssistant::class.java))
    }

    fun findByType(type: String): MutableList<BasAssistant> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FENTRYID,
                t1.FNUMBER,
                t2.FDATAVALUE,
                t3.FNAME as FType,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            FROM
                T_BAS_ASSISTANTDATAENTRY t1,
                T_BAS_ASSISTANTDATAENTRY_L t2,
                T_BAS_ASSISTANTDATA_L t3
            WHERE
                t1.FENTRYID = t2.FENTRYID
                AND t1.FID = t3.FID
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                AND t3.FNAME = :type
        """, Collections.singletonMap("type", type), BeanPropertyRowMapper(BasAssistant::class.java))
    }
}