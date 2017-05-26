package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.District
import org.springframework.data.jpa.repository.Query


interface DistrictRepository : BaseRepository<District, String> {

    fun findByParentId(parentId: String): MutableList<District>

    fun findByName(name: String): District

    @Query("select t from #{#entityName} t where t.province like %?1%  or t.city like %?1% or t.county like %?1%")
    fun findByNameLike(name: String): MutableList<District>

    fun findByProvinceAndCityAndCounty(province: String,city: String,county: String): District
}