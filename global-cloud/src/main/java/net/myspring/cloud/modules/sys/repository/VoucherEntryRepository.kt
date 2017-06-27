package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.common.repository.BaseRepository
import net.myspring.cloud.modules.sys.domain.VoucherEntry
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface VoucherEntryRepository : BaseRepository<VoucherEntry,String>{
    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE t.glVoucherId = :voucherId
     """)
    fun findByVoucherId(@Param("voucherId")voucherId:String):MutableList<VoucherEntry>
}