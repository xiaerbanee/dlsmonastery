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

    fun batchSave(imooProductImeiDeliverList:MutableList<ImooPrdocutImeiDeliver> ):IntArray{
        val sb = """ INSERT INTO imoo_prdocut_imei_deliver
            (company_id,mainagentid,agentid,ms_des,ms_id,msi_item,msi_item_des,msib_barcode,creation_date,box,imei)
            values (:companyId,:mainagentid,:agentid,:msDes,:msId,:msiItem,:msiItemDes,:msibBarcode,:creationDate,:box,:imei)
        """
        return namedParameterJdbcTemplate.batchUpdate(sb,SqlParameterSourceUtils.createBatch(imooProductImeiDeliverList.toTypedArray()))
    }

    fun findSendImeList(dateStart:String,dateEnd:String,agentCodeList: MutableList<String>):MutableList<ImooPrdocutImeiDeliver>{
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        map.put("agentCodeList",agentCodeList)
        val sb = """
            SELECT
                t.*
            FROM
                imoo_prdocut_imei_deliver t
            WHERE
                t.creation_date >= :dateStart
            AND t.creation_date < :dateEnd
            AND t.company_id IN (:agentCodeList)
        """
        return namedParameterJdbcTemplate.query(sb,map,BeanPropertyRowMapper(ImooPrdocutImeiDeliver::class.java))
    }


}
