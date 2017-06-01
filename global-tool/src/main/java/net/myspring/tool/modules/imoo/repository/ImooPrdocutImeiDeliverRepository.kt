package net.myspring.tool.modules.imoo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver
import org.springframework.cache.annotation.CacheConfig
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate


@Repository
 interface ImooPrdocutImeiDeliverRepository:BaseRepository<ImooPrdocutImeiDeliver,String> {

    @Query("""
            select t.imei
            from #{#entityName} t
            where t.imei in ?1
            """)
    fun findImeis(imeis: MutableList<String>): MutableList<String>


    @Query("""
            select t.*
            from #{#entityName} t
            where t.creation_date >= :dateStart
            and t.creation_date < :dateEnd
            and t.company_id in :agentCodes
            and t.imei not in (select p.ime from crm_product_ime p where p.company_id=1)
            """)
    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<ImooPrdocutImeiDeliver>

}
