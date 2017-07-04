package net.myspring.tool.modules.vivo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

import java.time.LocalDate;

@Component
class VivoPlantElectronicsnRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun updateProductImeRetailDate(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): Int{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);

        return namedParameterJdbcTemplate.update("""
            update crm_product_ime t1,#{#entityName} t2
            set t1.retail_date=t2.retail_date
            where t1.ime=t2.sn_imei
            and t2.retail_date >= :dateStart
            and t2.retail_date <= :dateEnd
        """,paramMap);
    }

    fun findSnImeis(snImeis: MutableList<String>): MutableList<String> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("snImeis", snImeis);
        return namedParameterJdbcTemplate.query("""
          select t.snImei
          from #{#entityName} t
          where t.snImei in :snImeis
        """,paramMap, BeanPropertyRowMapper(String::class.java));
    }

    fun findNameQtyList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): MutableList<VivoPlantElectronicsn>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);

        return namedParameterJdbcTemplate.query("""
          select t.product_id as name ,count(t.*) as qty
            from #{#entityName} t
            where t.product_id is not null
            and t.retailDate >=:dateStart
            and t.retailDate <=:dateEnd
            group by t.product_id
        """,paramMap, BeanPropertyRowMapper(VivoPlantElectronicsn::class.java));
    }
}
