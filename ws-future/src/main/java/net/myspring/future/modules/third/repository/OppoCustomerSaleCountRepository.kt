package net.myspring.future.modules.third.repository;

import com.google.common.collect.Maps
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.third.domain.OppoCustomerSaleCount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


/**
 * Created by admin on 2016/10/11.
 */
interface OppoCustomerSaleCountRepository : BaseRepository<OppoCustomerSaleCount, String>,OppoCustomerSaleCountRepositoryCustom {

}
interface OppoCustomerSaleCountRepositoryCustom{
    fun findAll(dateStart: String,dateEnd: String,companyId:String): MutableList<OppoCustomerSaleCount>
}
class OppoCustomerSaleCountRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerSaleCountRepositoryCustom {
    override fun findAll(dateStart: String, dateEnd: String, companyId:String): MutableList<OppoCustomerSaleCount>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("companyId",companyId);
        return namedParameterJdbcTemplate.query("""
             select
                        sa.shop_id as shopCode,
                        date_format(sa.created_date,'%Y-%m-%d') as saleTime,
                        im.product_id as productCode,
                        count(*) as qty
                    from
                        crm_product_ime_sale sa,crm_product_ime im
                    where
                            sa.product_ime_id=im.id
                            and sa.created_date >=:dateStart
                            and sa.created_date <=:dateEnd
                            and sa.is_back = 0
                            and sa.enabled = 1
                            and im.enabled=1
                            and sa.company_id =:companyId
                    group by
                            shopCode,saleTime,productCode
                 """, paramMap,BeanPropertyRowMapper(OppoCustomerSaleCount::class.java));
    }
}
