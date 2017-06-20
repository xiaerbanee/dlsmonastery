package net.myspring.tool.modules.oppo.repository;

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.third.domain.OppoCustomerSaleCount
import net.myspring.future.modules.third.domain.OppoCustomerSaleImei
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * Created by admin on 2016/10/11.
 */
interface OppoCustomerSaleCountRepository : BaseRepository<OppoCustomerSaleCount, String> {

    @Query("""
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
        """)
    fun findAll(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("companyId") companyId:String): MutableList<OppoCustomerSaleCount>

}
