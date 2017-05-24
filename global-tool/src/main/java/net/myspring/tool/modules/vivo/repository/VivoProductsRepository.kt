package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoProducts
import org.springframework.data.jpa.repository.Query
import java.util.List

/**
 * Created by admin on 2016/10/17.
 */
interface VivoProductsRepository: BaseRepository<VivoProducts, String> {

    @Query("""
        select
         t.color_id
         from vivo_products t
          where t.color_id in ?1
        """, nativeQuery = true)
    fun findColorIds(colorIds: List<String>): List<String>
}
