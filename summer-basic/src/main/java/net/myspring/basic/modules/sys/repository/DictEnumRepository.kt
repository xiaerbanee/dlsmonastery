package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.DictEnum
import net.myspring.basic.modules.sys.dto.DictEnumDto
import net.myspring.basic.modules.sys.dto.DictMapDto
import net.myspring.basic.modules.sys.web.query.DictEnumQuery
import net.myspring.basic.modules.sys.web.query.DictMapQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.persistence.EntityManager

/**
 * Created by haos on 2017/5/24.
 */
interface DictEnumRepository :BaseRepository<DictEnum,String>,DictEnumRepositoryCustom{
    @CachePut(key="#id")
    fun save(dictEnum: DictEnum): DictEnum

    @Cacheable
    override  fun findOne(id: String): DictEnum

    @Query("""
        SELECT
        t1.*
        FROM
        sys_dict_enum t1
        WHERE
        t1.enabled=1
        and t1.category=:category order by sort asc
     """, nativeQuery = true)
    fun findByCategory(@Param("category")category:String):MutableList<DictEnum>

    @Query("""
         SELECT DISTINCT
        t1.category
        FROM
        sys_dict_enum t1
     """, nativeQuery = true)
    fun findDistinctCategory():MutableList<String>

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


interface DictEnumRepositoryCustom{
    fun findPage(pageable: Pageable, dictEnumQuery: DictEnumQuery): Page<DictEnumDto>?


}

class DictEnumRepositoryImpl @Autowired constructor(val entityManager: EntityManager): DictEnumRepositoryCustom{
    override fun findPage(pageable: Pageable, dictEnumQuery: DictEnumQuery): Page<DictEnumDto>? {
        return null
    }


}