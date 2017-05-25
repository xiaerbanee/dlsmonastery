package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.CompanyConfig
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto
import net.myspring.basic.modules.sys.dto.CompanyConfigDto
import net.myspring.basic.modules.sys.dto.DictEnumDto
import net.myspring.basic.modules.sys.web.query.CompanyConfigQuery
import net.myspring.basic.modules.sys.web.query.DictEnumQuery
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
interface CompanyConfigRepository :BaseRepository<CompanyConfig,String>, CompanyConfigRepositoryCustom{
    @CachePut(key="#id")
    fun save(companyConfig: CompanyConfig): CompanyConfig

    @Cacheable
    override fun findOne(id: String): CompanyConfig

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

interface CompanyConfigRepositoryCustom{
    fun findPage(pageable: Pageable, companyConfigQuery: CompanyConfigQuery): Page<CompanyConfigDto>?


}

class CompanyConfigRepositoryImpl @Autowired constructor(val entityManager: EntityManager): CompanyConfigRepositoryCustom{
    override fun findPage(pageable: Pageable, companyConfigQuery: CompanyConfigQuery): Page<CompanyConfigDto>? {
        return null
    }


}