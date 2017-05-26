package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.CommonEntity;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Created by admin on 2016/10/17.
 */
interface VivoPlantElectronicsnRepository: BaseRepository<VivoPlantElectronicsn, String> {

    @Query("""
        update crm_product_ime t1,vivo_plant_electronicsn t2
        set t1.retail_date=t2.retail_date
        where t1.ime=t2.sn_imei
        and t2.retail_date >= :dateStart
        and t2.retail_date <= :dateEnd
        and t1.company_id = :companyId
        """, nativeQuery = true)
    fun updateProductImeRetailDate(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("companyId") companyId: Long?): Int

    @Query("""
      select t.sn_imei
      from vivo_plant_electronicsn t
      where t.sn_imei in :snImeis
        """, nativeQuery = true)
    fun findSnImeis(@Param("snImeis") snImeis: Collection<String>): List<String>

    @Query("""
        select
        t.product_id as name ,count(t.*) as qty
        from vivo_plant_electronicsn t
        where t.product_id is not null
        and t.retailDate >=:dateStart
        and t.retailDate <=:dateEnd
        group by t.product_id
        """, nativeQuery = true)
    fun findNameQtyList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): List<CommonEntity>
}
