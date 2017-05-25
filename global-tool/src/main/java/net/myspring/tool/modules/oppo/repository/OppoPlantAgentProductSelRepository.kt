package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import org.springframework.data.jpa.repository.Query

;

/**
 * Created by admin on 2016/10/11.
 */

interface OppoPlantAgentProductSelRepository:BaseRepository<OppoPlantAgentProductSel,String> {

    @Query("select t.item_number from oppo_plant_agent_product_sel t where t.item_number in ?1",nativeQuery = true)
    fun findItemNumbers(itemNumbers: MutableList<String>): MutableList<String>

}
