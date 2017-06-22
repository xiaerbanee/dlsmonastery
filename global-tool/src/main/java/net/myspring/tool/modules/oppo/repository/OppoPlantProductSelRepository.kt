package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class OppoPlantProductSelRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findColorIds(colorIds:MutableList<String>): MutableList<String> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("colorIds",colorIds);
        val  sql=" select t.colorId from #{#entityName} t  where t.colorId in :colorIds ";
        return namedParameterJdbcTemplate.query(sql,paramMap, BeanPropertyRowMapper(String::class.java));
    }
}
