package net.myspring.tool.modules.oppo.repository;

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.third.domain.OppoCustomerDemoPhone
import net.myspring.future.modules.third.domain.OppoCustomerStock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * Created by admin on 2016/10/11.
 */
interface OppoCustomerDemoPhoneRepository : BaseRepository<OppoCustomerDemoPhone, String> {

    @Query("""
         select
            demo.shop_id as customerid,
            demo.created_date as date,
            im.product_id as productCode,
            im.ime as imei
        from
            crm_demo_phone demo,
            crm_product_ime im
        where
            demo.product_ime_id = im.id
            and demo.created_date >=:dateStart
            and demo.created_date <=:dateEnd
            and demo.enabled = 1
            adn demo.company_id=:companyId
        """)
    fun findAll(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("companyId") companyId:String): MutableList<OppoCustomerDemoPhone>

}
