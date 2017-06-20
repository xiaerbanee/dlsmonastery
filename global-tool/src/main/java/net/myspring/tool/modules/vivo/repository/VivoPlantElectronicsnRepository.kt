package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Created by admin on 2016/10/17.
 */
@Repository
interface VivoPlantElectronicsnRepository: BaseRepository<VivoPlantElectronicsn, String> {

    @Query("""
        update crm_product_ime t1,#{#entityName} t2
        set t1.retail_date=t2.retail_date
        where t1.ime=t2.sn_imei
        and t2.retail_date >= :dateStart
        and t2.retail_date <= :dateEnd
        and t1.company_id = :companyId
        """)
    fun updateProductImeRetailDate(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("companyId") companyId: Long?): Int

    @Query("""
      select t.sn_imei
      from #{#entityName} t
      where t.sn_imei in :snImeis
        """)
    fun findSnImeis(@Param("snImeis") snImeis: Collection<String>): List<String>

    @Query("""
        select
        t.product_id as name ,count(t.*) as qty
        from #{#entityName} t
        where t.product_id is not null
        and t.retailDate >=:dateStart
        and t.retailDate <=:dateEnd
        group by t.product_id
        """)
    fun findNameQtyList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): List<VivoPlantElectronicsn>
}
