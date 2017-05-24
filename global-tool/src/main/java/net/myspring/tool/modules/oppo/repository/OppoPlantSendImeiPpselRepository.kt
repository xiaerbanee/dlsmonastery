package net.myspring.tool.modules.oppo.repository;

import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query

import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2016/10/11.
 */
interface OppoPlantSendImeiPpselMapper {


    @Query("select t.imei from oppo_plant_send_imei_ppsel t where t.imei in ?1",nativeQuery = true)
    fun findImeis(imeis: List<String>): List<String>

    @Query("""
        select t.*  from oppo_plant_send_imei_ppsel t
        where t.created_time >= #{dateStart}
        and t.created_time <= #{dateEnd}
        and t.company_id in :agentCodes
        and t.imei not in (select p.ime from crm_product_ime p )
        """, nativeQuery = true)
    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: List<String>): List<OppoPlantSendImeiPpsel>

}
