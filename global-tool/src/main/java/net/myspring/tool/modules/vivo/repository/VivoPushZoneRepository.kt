package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPushZones
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import java.time.LocalDate


interface VivoPushZoneRepository : BaseRepository<VivoPushZones, String>, VivoPushZoneRepositoryCustom {

}
interface VivoPushZoneRepositoryCustom{
    fun findBydate(dateStart:LocalDate,dateEnd:LocalDate):MutableList<VivoPushZones>
    fun batchSave(vivoPushZoneList: MutableList<VivoPushZones>):IntArray

}
class VivoPushZoneRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : VivoPushZoneRepositoryCustom{
    override fun findBydate(dateStart: LocalDate, dateEnd: LocalDate): MutableList<VivoPushZones> {
        val map = Maps.newHashMap<String,Any>();
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        return namedParameterJdbcTemplate.query("""
            SELECT *
            FROM vivo_push_zones
            WHERE created_date >= :dateStart
	            AND created_date < :dateEnd
        """,map,BeanPropertyRowMapper(VivoPushZones::class.java))
    }

    override fun batchSave(vivoPushZoneList: MutableList<VivoPushZones>): IntArray {
        val sb = StringBuilder();
        sb.append("insert into vivo_push_zones (zone_id,zone_name,short_cut,zone_depth,father_id,sub_count,zone_types,created_date)")
        sb.append("values (:zoneId,:zoneName,:shortCut,:zoneDepth,:fatherId,:subCount,:zoneTypes,:createdDate)")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(vivoPushZoneList.toTypedArray()));
    }
}
