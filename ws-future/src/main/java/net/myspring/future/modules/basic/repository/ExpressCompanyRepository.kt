package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.ExpressCompany
import net.myspring.future.modules.basic.dto.ExpressCompanyDto
import net.myspring.future.modules.basic.web.query.ExpressCompanyQuery
import org.apache.ibatis.annotations.*
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("expressCompanies"))
interface ExpressCompanyRepository : BaseRepository<ExpressCompany,String>{

    @Cacheable
    override fun findOne(id: String): ExpressCompany

    override fun findAll(): List<ExpressCompany>

    @CachePut(key = "#id")
    fun save(expressCompany: ExpressCompany): Int

    @Query("""
        SELECT t1.*
        FROM crm_express_company t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<ExpressCompany>

    @Query("""
        SELECT t1.*
        FROM crm_express_company t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: List<String>): List<ExpressCompany>

    fun findByExpressType(expressType: String): List<ExpressCompany>

    fun findByNameLike(@Param("companyId") companyId: String, @Param("name") name: String): List<ExpressCompanyDto>

    fun findByCompanyIdAndExpressType(@Param("companyId") companyId: String, @Param("expressType") expressType: String): List<ExpressCompanyDto>
}