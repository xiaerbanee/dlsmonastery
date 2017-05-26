
package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.DictMap
import net.myspring.basic.modules.sys.dto.DictMapDto
import net.myspring.basic.modules.sys.dto.MenuCategoryDto
import net.myspring.basic.modules.sys.web.query.DictMapQuery
import net.myspring.basic.modules.sys.web.query.MenuCategoryQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager

/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("dictMaps"))
interface  DictMapRepository :BaseRepository<DictMap,String>, DictMapRepositoryCustom{
    @CachePut(key="#id")
    fun save(dictMap: DictMap): DictMap

    @Cacheable
    override fun findOne(id: String): DictMap

    @Query("""
        SELECT DISTINCT
        t.category
        FROM  #{#entityName} t
     """)
    fun findDistinctCategory():MutableList<String>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE
        t.category=?1
        and t.enabled=1
     """, nativeQuery = true)
    fun findByCategory(category:String):MutableList<DictMap>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE
        t.name=?1
        and t.enabled=1
     """)
    fun findByName(name:String): DictMapDto


}


interface DictMapRepositoryCustom{
    fun findPage(pageable: Pageable, dictMapQuery: DictMapQuery): Page<DictMapDto>?


}

class DictMapRepositoryImpl @Autowired constructor(val entityManager: EntityManager): DictMapRepositoryCustom{
    override fun findPage(pageable: Pageable, dictMapQuery: DictMapQuery): Page<DictMapDto>? {
        return null
    }


}