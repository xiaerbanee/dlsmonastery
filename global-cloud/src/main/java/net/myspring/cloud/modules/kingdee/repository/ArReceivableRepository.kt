package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.common.config.MyBeanPropertyRowMapper
import net.myspring.cloud.modules.kingdee.domain.ArReceivable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class ArReceivableRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate){

    fun findBySourceBillNo(sourceBillNo: String): MutableList<ArReceivable> {
        return jdbcTemplate.query("""
            select
            t1.FBILLNO,
            t2.FSOURCEBILLNO
            from
            T_AR_RECEIVABLE t1,
            T_AR_RECEIVABLEENTRY t2
            where
            t1.fid=t2.FID
            and t2.FSOURCEBILLNO = :sourceBillNo
            order by t1.fid DESC
        """, MyBeanPropertyRowMapper(ArReceivable::class.java), sourceBillNo)
    }
}