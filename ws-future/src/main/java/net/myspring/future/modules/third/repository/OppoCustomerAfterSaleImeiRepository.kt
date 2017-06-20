package net.myspring.tool.modules.oppo.repository;

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.third.domain.OppoCustomerAfterSaleImei
import net.myspring.future.modules.third.domain.OppoCustomerStock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * Created by admin on 2016/10/11.
 */
interface OppoCustomerAfterSaleImeiRepository : BaseRepository<OppoCustomerAfterSaleImei, String> {

    @Query("""
         select
            af.bad_depot_id as customerid,
            af.created_date as date,
            im.product_id as productCode,
            im.ime as imei,
            0 as transType
        from
            crm_after_sale af,
            crm_product_ime im
        where
            af.bad_product_ime_id = im.id
            and af.created_date >=:dateStart
            and af.created_date <=:dateEnd
            and af.enabled = 1
            and af.company_id = :companyId
        """)
    fun findAll(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("companyId") companyId:String): MutableList<OppoCustomerAfterSaleImei>

}
