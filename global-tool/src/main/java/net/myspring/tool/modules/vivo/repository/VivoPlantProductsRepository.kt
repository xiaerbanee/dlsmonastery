package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


/**
 * Created by admin on 2016/10/17.
 */
@Repository
interface VivoPlantProductsRepository: BaseRepository<VivoPlantProducts, String> {

    @Query("""
        select t.item_number
        from #{#entityName} t
        where t.item_number in ?1
        """)
    fun findItemNumbers(itemNumbers: MutableCollection<String>): MutableList<String>
}
