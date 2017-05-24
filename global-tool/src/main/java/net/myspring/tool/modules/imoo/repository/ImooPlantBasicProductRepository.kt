package net.myspring.tool.modules.imoo.repository

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct
import org.springframework.data.jpa.repository.Query


interface ImooPlantBasicProductRepository: BaseRepository<ImooPlantBasicProduct, String> {

    @Query("select t.segment1 from imoo_plant_basic_product t where t.segment1 in ?1",nativeQuery = true)
    fun findSegment1s(segment1s: List<String>): List<String>


    @Query("select * from imoo_plant_basic_product where id = ?1",nativeQuery = true)
    fun findById(id: String): ImooPlantBasicProduct

}
