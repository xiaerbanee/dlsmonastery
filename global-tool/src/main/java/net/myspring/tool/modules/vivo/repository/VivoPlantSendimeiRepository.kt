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


    @Query("""
            select t.id,t.company_id,t.bill_id,t.product_id,t.imei,t.imei_state,t.remark,t.created_time,t.main_id,t.insert_time,t.chain_type,t.model,t.meid,t.imei2,pro.product_id as defaultProductId,pro.lx_product_id as lxProductId  from
            vivo_plant_sendimei t left join vivo_plant_products pro on t.product_id = pro.item_number
            where t.created_time >= #{dateStart}
            and t.created_time <= #{dateEnd}
            and t.company_id in :agentCodes
        """)
    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<VivoPlantSendimei>


}
