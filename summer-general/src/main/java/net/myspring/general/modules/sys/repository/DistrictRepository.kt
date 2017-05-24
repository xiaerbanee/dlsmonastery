package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.District
import org.springframework.data.jpa.repository.Query


interface DistrictRepository : BaseRepository<District, String> {

    fun findByParentId(parentId: String): List<District>

    fun findByName(name: String): District

    @Query("""
        SELECT t1.*
        FROM sys_district t1
        WHERE t1.province like %?1%  or t1.city like %?1% or t1.county like %?1%
        """, nativeQuery = true)
    fun findByNameLike(name: String): List<District>

    fun findByProvinceAndCityAndCounty(province: String,city: String,county: String): District
}