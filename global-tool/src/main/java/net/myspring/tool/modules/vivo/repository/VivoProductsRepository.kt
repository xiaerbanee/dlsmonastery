package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoProducts
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


/**
 * Created by admin on 2016/10/17.
 */
@Repository
interface VivoProductsRepository: BaseRepository<VivoProducts, String> {

    @Query("""
        select
         t.color_id
         from #{#entityName} t
          where t.color_id in ?1
        """)
    fun findColorIds(colorIds: MutableList<String>): MutableList<String>
}
