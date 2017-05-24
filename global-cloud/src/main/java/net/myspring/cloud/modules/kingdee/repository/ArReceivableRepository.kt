package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.ArReceivable
import org.springframework.data.jpa.repository.Query

interface ArReceivableRepository {

    @Query("""
       select
        t1.FBILLNO,
        t2.FSOURCEBILLNO
       from
        T_AR_RECEIVABLE t1,
        T_AR_RECEIVABLEENTRY t2
       where
        t1.fid=t2.FID
        and t2.FSOURCEBILLNO = ?1
        order by t1.fid DESC
        """, nativeQuery = true)
    fun findBySourceBillNo(sourceBillNo: String): List<ArReceivable>
}