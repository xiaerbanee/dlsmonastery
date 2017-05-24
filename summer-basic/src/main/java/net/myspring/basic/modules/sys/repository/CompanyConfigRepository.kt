package net.myspring.basic.modules.sys.repository

import net.myspring.basic.modules.sys.domain.CompanyConfig
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface CompanyConfigRepository{
    @CachePut(key="#id")
    fun save(companyConfig: CompanyConfig): CompanyConfig

    @Cacheable
    fun findOne(id: String): CompanyConfig

    @Query("""
        SELECT t1.*
        FROM sys_company_config t1
        where t1.enabled=1
        and t1.code=:code
     """, nativeQuery = true)
    fun  findByCode(@Param("code")code:String):CompanyConfig

    @Query("""
            SELECT t1.*
        FROM sys_company_config t1
        where t1.enabled=1
     """, nativeQuery = true)
    fun  findAllCache():List<CompanyConfigCacheDto>

}