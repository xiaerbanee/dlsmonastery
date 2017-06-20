package net.myspring.tool.modules.oppo.repository;

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.third.domain.OppoCustomerStock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * Created by admin on 2016/10/11.
 */
interface OppoCustomerStockRepository : BaseRepository<OppoCustomerStock, String> {

    @Query("""
          select
              de.id as customerid,
              pro.name as productcode ,
              count(*) as qty
          from
            crm_product_ime im left join crm_product_ime_upload up on im.product_ime_upload_id = up.id,crm_depot de,crm_product pro
            where
                im.depot_id = de.id
                and (
                    im.retail_date is null
                    or im.retail_date >:dateEnd
                )
                and (
                    up.id is null or up.created_date > :dateEnd
                )
                and im.company_id = :companyId
                and im.created_date>=:dateStart
                and im.created_date < :dateEnd
                and im.enabled = 1
                and de.enabled = 1
                and pro.enabled=1
                and im.product_id = pro.id
                group by de.id,pro.id asc
        """)
    fun findAll(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("companyId") companyId:String): MutableList<OppoCustomerStock>

}
