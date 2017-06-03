package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Company
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable

/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("companys"))
interface CompanyRepository :BaseRepository<Company,String>{
    @CachePut(key="#p0.id")
    fun save(company: Company): Company

    @Cacheable
    override  fun findOne(id: String): Company

}