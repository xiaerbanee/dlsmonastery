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
        select t from #{#entityName} t
        where t.createdTime >=:dateStart
        and t.createdTime <= :dateEnd
        and t.companyId in :agentCodes
        """)
    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<VivoPlantSendimei>

    @Query("""
        select t.imei
        from #{#entityName} t where t.imei in :imeiList
        """)
    fun findImeis(@Param("imeiList") imeiList: MutableList<String>): MutableSet<String>

    @Query("""
        select distinct t.productId
        from #{#entityName} t
        where t.createdTime> :dateStart
        and t.companyId in :agentCodes
        """)
    fun findErrorItemNumbers(@Param("dateStart") dateStart: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<String>
}
