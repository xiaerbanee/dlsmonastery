package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import org.springframework.data.jpa.repository.Query

;
import org.springframework.stereotype.Repository

/**
 * Created by admin on 2016/10/11.
 */
@Repository
interface OppoPlantAgentProductSelRepository:BaseRepository<OppoPlantAgentProductSel,String> {

    @Query("select t.item_number from #{#entityName} t where t.item_number in ?1")
    fun findItemNumbers(itemNumbers: MutableList<String>): MutableList<String>

}
