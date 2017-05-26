package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.modules.sys.domain.VoucherEntry
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface VoucherEntryRepository{
    @Query("""
        SELECT
        t1.*
        from
        sys_gl_voucher_entry t1
        WHERE
        t1.gl_voucher_id = :voucherId
     """, nativeQuery = true)
    fun findByGlVoucherId(@Param("voucherId")voucherId:String):MutableList<VoucherEntry>
}