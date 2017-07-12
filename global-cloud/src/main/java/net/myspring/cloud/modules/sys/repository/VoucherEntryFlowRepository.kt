package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.common.repository.BaseRepository
import net.myspring.cloud.modules.sys.domain.VoucherEntryFlow
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface  VoucherEntryFlowRepository : BaseRepository<VoucherEntryFlow,String>{
    @Query("""
        SELECT
        t1
        FROM  #{#entityName} t1
        WHERE
        t1.glVoucherEntryId = :voucherEntryId
     """)
    fun findByVoucherEntryId(@Param("voucherEntryId")voucherEntryId:String):MutableList<VoucherEntryFlow>?
}