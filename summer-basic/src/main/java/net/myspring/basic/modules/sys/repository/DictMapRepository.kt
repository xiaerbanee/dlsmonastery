
package net.myspring.basic.modules.sys.repository

import net.myspring.basic.modules.sys.domain.DictMap
import net.myspring.basic.modules.sys.dto.DictMapDto
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query

/**
 * Created by haos on 2017/5/24.
 */
interface  DictMapRepository{
    @CachePut(key="#id")
    fun save(dictMap: DictMap): DictMap

    @Cacheable
    fun findOne(id: String): DictMap

    @Query("""
         SELECT DISTINCT
        t1.category
        FROM
        sys_dict_map t1
     """, nativeQuery = true)
    fun findDistinctCategory():List<String>

    @Query("""
        SELECT
        t1.*
        FROM
        sys_dict_map t1
        WHERE
        t1.category=?1
        and t1.enabled=1
     """, nativeQuery = true)
    fun findByCategory(category:String):List<DictMap>

    @Query("""
         SELECT
        t1.*
        FROM
        sys_dict_map t1
        WHERE
        t1.name=?1
        and t1.enabled=1
     """, nativeQuery = true)
    fun findByName(name:String): DictMapDto

}