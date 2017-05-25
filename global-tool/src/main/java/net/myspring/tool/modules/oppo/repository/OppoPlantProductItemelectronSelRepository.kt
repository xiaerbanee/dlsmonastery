package net.myspring.tool.modules.oppo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2016/10/11.
 */
interface OppoPlantProductItemelectronSelRepository:BaseRepository<OppoPlantProductItemelectronSel,String> {

    @Query("""
        select t.product_no from oppo_plant_product_itemelectron_sel t
        where t.product_no in ?1
        """, nativeQuery = true)
    fun findProductNos(productNos: List<String>): List<String>

    @Query("""
        update crm_product_ime t1,oppo_plant_product_itemelectron_sel t2
        set t1.retail_date=t2.date_time
        where t1.ime=t2.product_no
        and t2.date_time >= :dateStart
        and t2.date_time <= :dateEnd
        and t1.company_id=1
        """, nativeQuery = true)
    fun updateItemNumber()

    @Query("""
        update crm_product_ime t1,oppo_plant_product_itemelectron_sel t2
        set t1.retail_date=t2.date_time
        where t1.ime=t2.product_no
        and t2.date_time >= :dateStart
        and t2.date_time <= :dateEnd
        and t1.company_id=1
        """, nativeQuery = true)
    fun updateProductImeRetailDate(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate)

}
