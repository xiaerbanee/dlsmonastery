package net.myspring.tool.modules.oppo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel
import org.springframework.data.jpa.repository.Query


/**
 * Created by admin on 2016/10/11.
 */
interface OppoPlantProductSelRepository:BaseRepository<OppoPlantProductSel,String>{

    @Query("  select t.color_id from oppo_plant_product_sel t where t.color_id in ?1",nativeQuery = true)
    fun findColorIds(colorIds: MutableList<String>): MutableList<String>

}
