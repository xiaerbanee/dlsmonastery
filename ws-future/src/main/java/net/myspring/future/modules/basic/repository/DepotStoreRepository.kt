package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DepotStore
import net.myspring.future.modules.basic.domain.ProductType
import net.myspring.future.modules.basic.dto.DepotStoreDto
import net.myspring.future.modules.basic.web.query.DepotStoreQuery
import net.myspring.util.text.StringUtils
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
@CacheConfig(cacheNames = arrayOf("depotStores"))
interface DepotStoreRepository : BaseRepository<DepotStore,String>,DepotStoreRepositoryCustom{

    @Cacheable
    override fun findOne(id: String): DepotStore

    override fun findAll(): MutableList<DepotStore>

    @CachePut(key = "#p0.id")
    fun save(depotStore: DepotStore): DepotStore

    @Query("""
        SELECT t1.*
        FROM crm_depot_store t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<DepotStore>

    @Query("""
        SELECT t1.*
        FROM crm_depot_store t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: MutableList<String>): MutableList<DepotStore>

}

interface DepotStoreRepositoryCustom{
    fun findPage(pageable: Pageable, depotStoreQuery: DepotStoreQuery): Page<DepotStoreDto>
}

class DepotStoreRepositoryImpl @Autowired constructor(val entityManager: EntityManager):DepotStoreRepositoryCustom{

    override fun findPage(pageable: Pageable, depotStoreQuery: DepotStoreQuery): Page<DepotStoreDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t2.*, t1.id AS 'depotId',
                t1.office_id AS 'officeId',
                t1.tax_name AS 'taxName',
                t1.delegate_depot_id AS 'delegateDepotId',
                t1.pop_shop AS 'popShop'
            FROM
                crm_depot t1,
                crm_depot_store t2
            WHERE
                t1.enabled = 1
            AND t2.enabled = 1
            AND t2.depot_id = t1.id
        """)
        if (StringUtils.isNotEmpty(depotStoreQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), DepotStoreDto::class.java)

        return query.resultList as Page<DepotStoreDto>
    }
}