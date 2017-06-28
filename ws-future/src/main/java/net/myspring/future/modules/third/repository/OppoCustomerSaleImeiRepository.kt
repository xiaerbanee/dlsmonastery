package net.myspring.future.modules.third.repository;

import com.google.common.collect.Maps
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.third.domain.OppoCustomerSaleImei
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerSaleImeiRepository : BaseRepository<OppoCustomerSaleImei, String>,OppoCustomerSaleImeiRepositoryCustom {

}
interface OppoCustomerSaleImeiRepositoryCustom{
    fun findAll(dateStart: String,dateEnd: String,companyId:String): MutableList<OppoCustomerSaleImei>
}
class OppoCustomerSaleImeiRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerSaleImeiRepositoryCustom {
    override fun findAll(dateStart: String, dateEnd: String, companyId: String): MutableList<OppoCustomerSaleImei> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);
        paramMap.put("companyId", companyId);

        return namedParameterJdbcTemplate.query("""
             select
                    im.ime as imei,
                    sa.created_date as saletime,
                    sa.buyer as custname,
                    sa.buyer_phone as custmobile,
                    sa.buyer_sex as custsex,
                    sa.created_by as accountId,
                    de.id as shopcode,
                    de.name as shopname,
                    de.district_id as districtId
            from
                    crm_product_ime_sale sa,
                    crm_product_ime im,
                    crm_depot de
            where
                    sa.created_date >=:dateStart
                    and sa.created_date <= :dateEnd
                    and sa.product_ime_id = im.id
                    and sa.shop_id = de.id
                    and sa.is_back = 0
                    and sa.enabled = 1
                    and sa.company_id = :companyId
                """, paramMap, BeanPropertyRowMapper(OppoCustomerSaleImei::class.java));
    }
}