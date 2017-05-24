package net.myspring.basic.modules.sys.repository

import net.myspring.basic.modules.sys.domain.Company
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable

/**
 * Created by haos on 2017/5/24.
 */
interface CompanyRepository{
    @CachePut(key="#id")
    fun save(company: Company): Company

    @Cacheable
    fun findOne(id: String): Company

}