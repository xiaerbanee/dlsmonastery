package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.dataSource.DbContextHolder
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcCall

interface OppoPlantProductSelRepository : BaseRepository<OppoPlantProductSel, String>, OppoPlantProductSelRepositoryCustom {

    @Query("select t from #{#entityName}  t where t.colorId in (?1)")
    fun findColorIds(colorIds:MutableList<String>): MutableList<OppoPlantProductSel>

}
interface OppoPlantProductSelRepositoryCustom{
    fun plantProductSel(companyId: String,password: String, branchId: String): MutableList<OppoPlantProductSel>
}

class OppoPlantProductSelRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,val jdbcTemplate: JdbcTemplate) : OppoPlantProductSelRepositoryCustom{

    override fun plantProductSel(companyId: String, password: String, branchId: String): MutableList<OppoPlantProductSel> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("agentId",companyId);
        paramMap.put("pwd",password);
        paramMap.put("brandId",branchId);
        val simpleJdbcCall=SimpleJdbcCall(jdbcTemplate);
        return simpleJdbcCall.withProcedureName("plantProductSel").returningResultSet("returnValue",BeanPropertyRowMapper(OppoPlantProductSel::class.java)).execute(paramMap).get("returnValue") as MutableList<OppoPlantProductSel>;
    }
}
