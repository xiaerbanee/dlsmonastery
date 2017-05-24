package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.AdApply
import net.myspring.future.modules.layout.dto.AdApplyDto
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

/**
 * Created by zhangyf on 2017/5/24.
 */
interface AdApplyRepository : BaseRepository<AdApply,String> {

    fun findByOutGroupIdAndDate(@Param("dateStart") dateStart: LocalDate, @Param("outGroupIds") outGroupIds: List<String>): List<AdApplyDto>

    @Query("""
        select t.id
        from crm_ad_apply t
        where t.enabled = 1
    """, nativeQuery = true)
    fun findAllId(): List<String>

    fun findByFilter(@Param("p") map: Map<String, Any>): List<AdApply>
}