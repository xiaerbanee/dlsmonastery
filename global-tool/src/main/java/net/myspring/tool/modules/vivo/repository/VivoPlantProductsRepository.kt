package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface VivoPlantProductsRepository : BaseRepository<VivoPlantProducts, String>, VivoPlantProductsRepositoryCustom {

    @Query("select  t from #{#entityName}  t where t.itemNumber in (?1)")
    fun findItemNumbers(itemNumbers: MutableCollection<VivoPlantProducts>): MutableList<VivoPlantProducts>
}
interface VivoPlantProductsRepositoryCustom{

}
class VivoPlantProductsRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :VivoPlantProductsRepositoryCustom {

}
