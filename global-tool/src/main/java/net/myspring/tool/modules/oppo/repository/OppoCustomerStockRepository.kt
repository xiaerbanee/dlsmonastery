package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerSale
import net.myspring.tool.modules.oppo.domain.OppoCustomerStock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerStockRepository : BaseRepository<OppoCustomerStock, String>, OppoCustomerStockRepositoryCustom {

}
interface OppoCustomerStockRepositoryCustom{
    fun findAll(dateStart: String,dateEnd: String): MutableList<OppoCustomerStock>
    fun findByDate(dateStart:String,dateEnd:String):MutableList<OppoCustomerStock>

}
class OppoCustomerStockRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerStockRepositoryCustom{
    override fun findAll(dateStart: String, dateEnd: String): MutableList<OppoCustomerStock> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);
        return namedParameterJdbcTemplate.query("""
             select
                  de.id as customerid,
                  pro.id as productcode ,
                  count(*) as qty,
                 :dateEnd as date
              from
                crm_product_ime im left join crm_product_ime_upload up on im.product_ime_upload_id = up.id,crm_depot de,crm_product pro
                where
                    im.depot_id is not null
                    and im.depot_id = de.id
                    and im.created_date>=:dateStart
                    and im.created_date<:dateEnd
                    and im.enabled = 1
                    and (
                        im.retail_date is null
                        or im.retail_date >:dateEnd
                    )
                    and (
                        up.id is null or up.created_date > :dateEnd
                    )
                    and pro.enabled=1
                    and im.product_id = pro.id
                    group by de.id,pro.id asc
            """, paramMap, BeanPropertyRowMapper(OppoCustomerStock::class.java));
    }

    override fun findByDate(dateStart:String, dateEnd:String): MutableList<OppoCustomerStock> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_stock where  date >=:dateStart and date <:dateEnd
         """,paramMap,BeanPropertyRowMapper(OppoCustomerStock::class.java));
    }
}
