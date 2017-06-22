package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query

;
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

/**
 * Created by admin on 2016/10/11.
 */
@Component
class OppoPlantAgentProductSelRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findItemNumbers(itemNumbers:MutableList<String>): MutableList<String> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("itemNumbers",itemNumbers);
        val  sql=" select t.itemNumber from #{#entityName} t where t.itemNumber in :itemNumbers";
        return namedParameterJdbcTemplate.query(sql,paramMap,BeanPropertyRowMapper(String::class.java));
    }

    fun findAllByProductId(): MutableList<OppoPlantAgentProductSel> {
        val  sql="select * from #{#entityName} t  where t.productId is not null or t.lxProductId is not null";
        return namedParameterJdbcTemplate.query(sql,BeanPropertyRowMapper(OppoPlantAgentProductSel::class.java));
    }
}
