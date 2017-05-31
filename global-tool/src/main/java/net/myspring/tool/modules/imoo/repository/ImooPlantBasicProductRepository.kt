package net.myspring.tool.modules.imoo.repository

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ImooPlantBasicProductRepository: BaseRepository<ImooPlantBasicProduct, String> {

    @Query("""
            select t.segment1
            from #{#entityName} t
            where t.segment1 in ?1
             """)
    fun findSegment1s(segment1s: MutableList<String>): MutableList<String>


    @Query("""
            select t.*
            from  #{#entityName} t
            where id = ?1
            """)
    fun findById(id: String): ImooPlantBasicProduct

}
