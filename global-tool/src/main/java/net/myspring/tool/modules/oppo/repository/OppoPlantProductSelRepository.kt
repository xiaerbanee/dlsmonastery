package net.myspring.tool.modules.oppo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel
import org.springframework.data.jpa.repository.Query


/**
 * Created by admin on 2016/10/11.
 */
interface OppoPlantProductSelRepository:BaseRepository<OppoPlantProductSel,String>{

    @Query("  select t.color_id from #{#entityName} t where t.color_id in ?1")
    fun findColorIds(colorIds: MutableList<String>): MutableList<String>

}
