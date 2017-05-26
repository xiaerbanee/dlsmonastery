package net.myspring.tool.modules.oppo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

import java.time.LocalDate;
;

/**
 * Created by admin on 2016/10/11.
 */
interface OppoPlantSendImeiPpselRepository:BaseRepository<OppoPlantSendImeiPpsel,String> {


    @Query("select t.imei from oppo_plant_send_imei_ppsel t where t.imei in ?1",nativeQuery = true)
    fun findImeis(imeis: MutableList<String>): MutableList<String>

    @Query("""
        select t.id,t.company_id,t.bill_id,t.imei,t.meid,t.created_time,t.dls_product_id,t.imei_state,t.remarks,t.ime2,se.product_id as productId,se.lx_product_id as lxProductId,se.color_id as colorId from oppo_plant_send_imei_ppsel t left join oppo_plant_agent_product_sel sel on t.dls_product_id=se.item_number
        where t.created_time >= #{dateStart}
        and t.created_time <= #{dateEnd}
        and t.company_id in :agentCodes
        """, nativeQuery = true)
    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<OppoPlantSendImeiPpsel>

}
