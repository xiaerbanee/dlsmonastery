package net.myspring.tool.modules.oppo.repository;

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.third.domain.OppoCustomerSaleImei
import net.myspring.future.modules.third.domain.OppoCustomerStock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * Created by admin on 2016/10/11.
 */
interface OppoCustomerSaleImeiRepository : BaseRepository<OppoCustomerSaleImei, String> {

    @Query("""
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
        """)
    fun findAll(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("companyId") companyId:String): MutableList<OppoCustomerSaleImei>

}
