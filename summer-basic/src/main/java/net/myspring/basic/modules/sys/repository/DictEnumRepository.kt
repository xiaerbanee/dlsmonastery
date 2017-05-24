package net.myspring.basic.modules.sys.repository

import net.myspring.basic.modules.sys.domain.DictEnum
import net.myspring.basic.modules.sys.dto.DictEnumDto
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface DictEnumRepository{
    @CachePut(key="#id")
    fun save(dictEnum: DictEnum): DictEnum

    @Cacheable
    fun findOne(id: String): DictEnum

    @Query("""
        SELECT
        t1.*
        FROM
        sys_dict_enum t1
        WHERE
        t1.enabled=1
        and t1.category=:category order by sort asc
     """, nativeQuery = true)
    fun findByCategory(@Param("category")category:String):List<DictEnum>

    @Query("""
         SELECT DISTINCT
        t1.category
        FROM
        sys_dict_enum t1
     """, nativeQuery = true)
    fun findDistinctCategory():List<String>

    @Query("""
        SELECT
        t1.*
        FROM
        sys_dict_enum t1
        WHERE
        t1.enabled=1
        and t1.value=:value
     """, nativeQuery = true)
    fun findByValue(@Param("value")value:String): DictEnumDto
}