package net.myspring.tool.modules.oppo.repository;

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.third.domain.OppoCustomerSale
import net.myspring.future.modules.third.domain.OppoCustomerStock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate


/**
 * Created by admin on 2016/10/11.
 */
interface OppoCustomerSaleRepository : BaseRepository<OppoCustomerSale, String> {

    @Query("""
            select
                sa.shop_id as customerId,
                date_format(sa.created_date,'%Y-%m-%d') as date,
                count(*) as totalSaleQty
            from
                crm_product_ime_sale sa
            where
                sa.created_date >=:dateStart
                and sa.created_date <=:dateEnd
                and sa.is_back = 0
                and sa.enabled = 1
                and sa.company_id =:companyId
            group by
                sa.shop_id,date_format(sa.created_date,'%Y-%m-%d')
            order by sa.shop_id,date_format(sa.created_date,'%Y-%m-%d')  asc
        """)
    fun findAll(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("companyId") companyId:String): MutableList<OppoCustomerSale>

}
