package net.myspring.tool.modules.imoo.repository

import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate


@Repository
interface ImooRepository {

    @Query("select * from PlantBasicProduct",nativeQuery = true)
    fun plantBasicProducts(): MutableList<ImooPlantBasicProduct>

    @Query("""
        select * from prdocutimeideliver t
        where t.creation_date >= :dateStart
        and t.creation_date < :dateEnd
        and mainagentid in :agentCodes
        """, nativeQuery = true)
    fun plantPrdocutImeiDeliverByDate(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<ImooPrdocutImeiDeliver>

}