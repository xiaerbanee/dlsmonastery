package net.myspring.tool.modules.imoo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Component
class ImooPrdocutImeiDeliverRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findImeis(imeis: MutableList<String>): MutableList<String>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("imeis", imeis);
        return namedParameterJdbcTemplate.query("""
           select t.imei
            from imoo_prdocut_imei_deliver t
            where t.imei in (:imeis)
            """, paramMap, BeanPropertyRowMapper(String::class.java));
    }


    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<ImooPrdocutImeiDeliver>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);
        paramMap.put("agentCodes", agentCodes);
        return namedParameterJdbcTemplate.query("""
         select
            de.*,map.product_id as productId
        from
            imoo_prdocut_imei_deliver de
        left join imoo_plant_basic_product bas on de.msi_item = bas.segment1
        left join imoo_product_map map on bas.id = map.imoo_plant_basic_product_id
        where
            de.creation_date >=:dateStart
            and de.creation_date <=:dateEnd
            and de.company_id=:agentCodes
            """, paramMap, BeanPropertyRowMapper(ImooPrdocutImeiDeliver::class.java));

    }

    fun batchSave(imooProductImeiDeliverList:MutableList<ImooPrdocutImeiDeliver> ):IntArray{
        val sb = """ INSERT INTO imoo_prdocut_imei_deliver
            (company_id,mainagentid,agentid,ms_des,ms_id,msi_item,msi_item_des,msib_barcode,creation_date,box,imei)
            valus (:companyId,:mainagentid,:agentid,:msDes,:msId,:msiItem,:msiItemDes,:msibBarcode,:creationDate,:box,:imei)
        """
        return namedParameterJdbcTemplate.batchUpdate(sb,SqlParameterSourceUtils.createBatch(imooProductImeiDeliverList.toTypedArray()))
    }

}
