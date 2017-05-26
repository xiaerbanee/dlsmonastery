package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

import java.time.LocalDate;

/**
 * Created by admin on 2016/10/17.
 */
interface VivoPlantSendimeiRepository: BaseRepository<VivoPlantSendimei, String> {

    @Query("""
        select t.* from vivo_plant_sendimei t
        where t.created_time >= :dateStart
        and t.created_time  <= :dateEnd
        and t.company_id in :agentCodes
        and t.imei not in (select p.ime from crm_product_ime p where p.company_id= :companyId)
        """, nativeQuery = true)
    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>, @Param("companyId") companyId: Long?): MutableList<VivoPlantSendimei>

    @Query("""
        select t.imei
        from vivo_plant_sendimei t where t.imei in :imeiList
        """, nativeQuery = true)
    fun findImeis(@Param("imeiList") imeiList: MutableList<String>): MutableSet<String>

    @Query("""
        select distinct t.productId
        from vivo_plant_sendimei t
        where t.created_time > :createdTime
        and t.imei not in (select p.ime from ProductIme p where p.company.id= :companyId )
        and t.companyId in :agentCodes
        """, nativeQuery = true)
    fun findErrorItemNumbers(@Param("dateStart") dateStart: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>, @Param("companyId") companyId: Long?): MutableList<String>
}
