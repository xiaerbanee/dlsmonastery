package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.time.LocalDate;

/**
 * Created by admin on 2016/10/17.
 */
@Repository
interface VivoPlantSendimeiRepository: BaseRepository<VivoPlantSendimei, String> {

    @Query("""
        select t.* from #{#entityName} t
        where t.created_time >= :dateStart
        and t.created_time  <= :dateEnd
        and t.company_id in :agentCodes
        and t.imei not in (select p.ime from crm_product_ime p where p.company_id= :companyId)
        """)
    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>, @Param("companyId") companyId: Long?): MutableList<VivoPlantSendimei>

    @Query("""
        select t.imei
        from #{#entityName} t where t.imei in :imeiList
        """)
    fun findImeis(@Param("imeiList") imeiList: MutableList<String>): MutableSet<String>

    @Query("""
        select distinct t.productId
        from #{#entityName} t
        where t.created_time > :createdTime
        and t.imei not in (select p.ime from ProductIme p where p.company.id= :companyId )
        and t.companyId in :agentCodes
        """)
    fun findErrorItemNumbers(@Param("dateStart") dateStart: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>, @Param("companyId") companyId: Long?): MutableList<String>
}
