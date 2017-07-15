package net.myspring.tool.modules.vivo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.dto.VivoPlantSendimeiDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import java.lang.StringBuilder

import java.time.LocalDate;

interface VivoPlantSendimeiRepository : BaseRepository<VivoPlantSendimei, String>, VivoPlantSendimeiRepositoryCustom {

}
interface VivoPlantSendimeiRepositoryCustom{
    fun findSynList(@Param("dateStart") dateStart: String, @Param("dateEnd") dateEnd: String, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<VivoPlantSendimeiDto>
    fun findPlantSendimei(dateStart: String, dateEnd: String, agentCodes: MutableList<String>): MutableList<VivoPlantSendimei>
    fun findImeis( imeiList: MutableList<String>): MutableList<VivoPlantSendimei>
    fun batchSave(pullPlantSendimeis:MutableList<VivoPlantSendimei>): IntArray?
}

class VivoPlantSendimeiRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :VivoPlantSendimeiRepositoryCustom {
    override fun findSynList(@Param("dateStart") dateStart: String, @Param("dateEnd") dateEnd: String, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<VivoPlantSendimeiDto>{
        var paramMap=Maps.newHashMap<String,Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("agentCodes",agentCodes);

        return namedParameterJdbcTemplate.query("""
           select
                t.*, g.color_id as colorId,
                g.product_id as defaultProductId,
                g.lx_product_id as lxProductId
            from
                vivo_plant_sendimei t,
                vivo_plant_products g
            where
                t.product_id = g.item_number
            and t.created_time >=:dateStart
            and t.created_time <=:dateEnd
            and t.company_id in (:agentCodes)
        """,paramMap,BeanPropertyRowMapper(VivoPlantSendimeiDto::class.java));
    }

    override fun findPlantSendimei(dateStart: String, dateEnd: String, agentCodes: MutableList<String>): MutableList<VivoPlantSendimei>{
        var paramMap=Maps.newHashMap<String,Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("agentCodes",agentCodes);
        System.err.println("dateStart=="+dateStart+"\tdateEnd=="+dateEnd+"\tagentCodes=="+agentCodes.toString());
        return namedParameterJdbcTemplate.query("""
      select *  from vr_plant_sendimei_m13e00 t1
        where t1.createdtime >= :dateStart
        and t1.createdtime < :dateEnd
        and t1.companyid in (:agentCodes)
      """,paramMap,BeanPropertyRowMapper(VivoPlantSendimei::class.java));
    }

    override fun findImeis(imeiList:MutableList<String>): MutableList<VivoPlantSendimei>{
        var paramMap= Maps.newHashMap<String,Any>();
        paramMap.put("imeiList",imeiList);
        return namedParameterJdbcTemplate.query("""
                select *  from vivo_plant_sendimei  where imei in(:imeiList)
        """,  paramMap,BeanPropertyRowMapper(VivoPlantSendimei::class.java));
    }

    override fun batchSave(pullPlantSendimeis: MutableList<VivoPlantSendimei>): IntArray? {
        val sb = StringBuilder()
        sb.append("insert into vivo_plant_sendimei (company_id,bill_id,product_id,imei,imei_state,remark,created_time,main_id,insert_time,chain_type,model,meid,imei2) values")
        sb.append("(:companyId,:billId,:productId,:imei,:imeiState,:remark,:createdTime,:mainId,:insertTime,:chainType,:model,:meid,:imei2)")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(), SqlParameterSourceUtils.createBatch(pullPlantSendimeis.toTypedArray()));
    }
}