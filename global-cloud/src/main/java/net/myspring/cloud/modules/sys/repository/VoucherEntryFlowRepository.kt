package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.modules.sys.domain.VoucherEntryFlow
import org.springframework.data.jpa.repository.Query

/**
 * Created by haos on 2017/5/24.
 */
interface  VoucherEntryFlowRepository{
    @Query("""
        SELECT
        t1.*
        from
        sys_gl_voucher_entry_flow t1
        WHERE
        t1.gl_voucher_entry_id = :voucherEntryId
     """, nativeQuery = true)
    fun findByVoucherEntryId(voucherEntryId:String):List<VoucherEntryFlow>
}