package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.ExpressCompany
import net.myspring.future.modules.basic.dto.DepotStoreDto
import net.myspring.future.modules.basic.dto.ExpressCompanyDto
import net.myspring.future.modules.basic.web.query.DepotStoreQuery
import net.myspring.future.modules.basic.web.query.ExpressCompanyQuery
import org.apache.ibatis.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("expressCompanies"))
interface ExpressCompanyRepository : BaseRepository<ExpressCompany,String>,ExpressCompanyRepositoryCustom{

    @Cacheable
    override fun findOne(id: String): ExpressCompany

    override fun findAll(): MutableList<ExpressCompany>

    @CachePut(key = "#id")
    fun save(expressCompany: ExpressCompany): Int

    @Query("""
        SELECT t1.*
        FROM crm_express_company t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<ExpressCompany>

    @Query("""
        SELECT t1.*
        FROM crm_express_company t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: MutableList<String>): MutableList<ExpressCompany>

    fun findByExpressType(expressType: String): MutableList<ExpressCompany>

//    fun findByNameLike(@Param("companyId") companyId: String, @Param("name") name: String): MutableList<ExpressCompanyDto>

}

interface ExpressCompanyRepositoryCustom{
    fun findPage(pageable: Pageable, expressCompanyQuery: ExpressCompanyQuery): Page<ExpressCompanyDto>
}

class ExpressCompanyRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ExpressCompanyRepositoryCustom{

    override fun findPage(pageable: Pageable, expressCompanyQuery: ExpressCompanyQuery): Page<ExpressCompanyDto> {
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), ExpressCompanyDto::class.java)

        return query.resultList as Page<ExpressCompanyDto>
    }
}