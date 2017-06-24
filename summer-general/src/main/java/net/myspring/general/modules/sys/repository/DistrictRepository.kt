package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.District
import net.myspring.general.modules.sys.domain.Town
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query

@CacheConfig(cacheNames = arrayOf("districts"))
interface DistrictRepository : BaseRepository<District, String> {

    @Cacheable
    override fun findOne(id: String): District

    @CachePut(key = "#p0.id")
    fun save(district: District): District

    fun findByParentId(parentId: String): MutableList<District>

    fun findByName(name: String): District

    @Query("select t from #{#entityName} t where t.province like %?1%  or t.city like %?1% or t.county like %?1%")
    fun findByNameLike(name: String): MutableList<District>

    fun findByProvinceAndCityAndCounty(province: String,city: String,county: String): District

    fun findByIdIn(idList: MutableList<String>): MutableList<District>

}