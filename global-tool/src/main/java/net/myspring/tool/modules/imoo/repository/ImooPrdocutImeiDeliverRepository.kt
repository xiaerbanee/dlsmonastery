package net.myspring.tool.modules.imoo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate


@Repository
 interface ImooPrdocutImeiDeliverRepository:BaseRepository<ImooPrdocutImeiDeliver,String> {

    @Query("""
            select t.imei
            from #{#entityName} t
            where t.imei in ?1
            """)
    fun findImeis(imeis: MutableList<String>): MutableList<String>


    @Query("""
        select
            de.*,map.product_id as productId
        from
            imoo_prdocut_imei_deliver de
        left join imoo_plant_basic_product bas ON de.msi_item = bas.segment1
        left join imoo_product_map map ON bas.id = map.imoo_plant_basic_product_id
        where
            de.creation_date >=:dateStart
            and de.creation_date <=:dateEnd
            and de.company_id=:agentCodes
            """)
    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<ImooPrdocutImeiDeliver>

}
