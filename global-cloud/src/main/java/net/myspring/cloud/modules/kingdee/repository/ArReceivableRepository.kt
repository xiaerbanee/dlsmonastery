package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.ArReceivable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class ArReceivableRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findBySourceBillNo(sourceBillNo: String): MutableList<ArReceivable> {
        return namedParameterJdbcTemplate.query("""
            select
            t1.FBILLNO,
            t2.FSOURCEBILLNO,
            t1.FDOCUMENTSTATUS
            from
            T_AR_RECEIVABLE t1,
            T_AR_RECEIVABLEENTRY t2
            where
            t1.fid=t2.FID
            and t2.FSOURCEBILLNO = :sourceBillNo
            order by t1.fid DESC
        """,Collections.singletonMap("sourceBillNo",sourceBillNo), BeanPropertyRowMapper(ArReceivable::class.java))
    }

    fun findTopOneBySourceBillNo(sourceBillNo: String): ArReceivable {
        return namedParameterJdbcTemplate.queryForObject("""
            select top 1
            t1.FBILLNO,
            t2.FSOURCEBILLNO,
            t1.FDOCUMENTSTATUS
            from
            T_AR_RECEIVABLE t1,
            T_AR_RECEIVABLEENTRY t2
            where
            t1.fid=t2.FID
            and t2.FSOURCEBILLNO = :sourceBillNo
            order by t1.fid DESC
        """,Collections.singletonMap("sourceBillNo",sourceBillNo), BeanPropertyRowMapper(ArReceivable::class.java))
    }
}