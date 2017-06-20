package net.myspring.tool.modules.oppo.repository;

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.third.domain.OppoCustomerAllot
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * Created by admin on 2016/10/11.
 */
interface OppoCustomerAllotRepository : BaseRepository<OppoCustomerAllot, String> {

    @Query("""
             select
                go.store_id as fromCustomerid,go.shop_id as toCustomerid,de.product_id as productcode,sum(de.shipped_qty) as qty
                from
                    crm_goods_order go,
                    crm_goods_order_detail de
                where
                    go.id = de.goods_order_id
                    and go.ship_date>=:dateStart
                    and go.ship_date<=:dateEnd
                    and de.shipped_qty >0
                    and go.enabled=1
                    and go.company_id=:companyId
                    group by go.store_id,go.shop_id,de.product_id
            union
            select
                st.from_store_id as fromCustomerid,st.to_store_id as toCustomerid,de.product_id as productcode,sum(de.shipped_qty) as qty
                from
                    crm_store_allot st,
                    crm_store_allot_detail de
                where
                    de.store_allot_id=st.id
                    and st.ship_date>=:dateStart
                    and st.ship_date<=:dateEnd
                    and de.shipped_qty>0
                    and st.enabled=1
                    and st.company_id=:companyId
                    group by st.from_store_id,st.to_store_id,de.product_id
            union
            select
                t.from_depot_id as fromCustomerid,t.to_depot_id as toCustomerid,im.product_id as productcode,count(*) as qty
                from
                    crm_ime_allot t,crm_product_ime im
                where
                    t.audit_date >= :dateStart
                    and t.audit_date <= :dateEnd
                    and t.company_id = :companyId
                    and t.enabled = 1
                    and t.`status` = '已通过'
                    and t.product_ime_id=im.id
                    group by t.from_depot_id,t.to_depot_id,im.product_id
        """)
    fun findAll(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("companyId") companyId:String): MutableList<OppoCustomerAllot>

}
