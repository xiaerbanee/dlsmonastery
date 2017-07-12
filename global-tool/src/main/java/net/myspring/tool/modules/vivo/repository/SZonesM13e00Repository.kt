package net.myspring.tool.modules.vivo.repository

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.SZonesM13e00
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils


interface SZonesM13e00Repository : BaseRepository<SZonesM13e00, String>, SZonesM13e00RepositoryCustom {

}
interface SZonesM13e00RepositoryCustom{
    fun batchSave(sZonesM13e00List: MutableList<SZonesM13e00>):IntArray?
}

class SZonesM13e00RepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : SZonesM13e00RepositoryCustom{
    override fun batchSave(sZonesM13e00List: MutableList<SZonesM13e00>): IntArray {
        val sb = StringBuilder()
        sb.append("insert into S_ZONEs_M13E00 (zoneID,zoneName,shortCut,zoneDepth,zonePath,fatherID,subCount,zoneTypes)")
        sb.append("values (:zoneID,:zoneName,:shortCut,:zoneDepth,:zonePath,:fatherID,:subCount,:zoneTypes)")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sZonesM13e00List.toTypedArray()))
    }
}
