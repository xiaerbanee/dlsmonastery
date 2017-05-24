package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts
import org.springframework.data.jpa.repository.Query
import java.util.Collection
import java.util.List

/**
 * Created by admin on 2016/10/17.
 */
interface VivoPlantProductsRepository: BaseRepository<VivoPlantProducts, String> {

    @Query("""
        select t.item_number
        from vivo_plant_products t
        where t.item_number in ?1
        """, nativeQuery = true)
    fun findItemNumbers(itemNumbers: Collection<String>): List<String>
}
