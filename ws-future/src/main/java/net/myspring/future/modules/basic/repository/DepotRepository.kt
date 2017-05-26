package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Depot
import net.myspring.future.modules.basic.dto.DepotAccountDto
import net.myspring.future.modules.basic.dto.DepotDto
import net.myspring.future.modules.basic.web.query.DepotAccountQuery
import net.myspring.future.modules.basic.web.query.DepotQuery
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
@CacheConfig(cacheNames = arrayOf("depots"))
interface DepotRepository :BaseRepository<Depot,String>,DepotRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): Depot

    override fun findAll(): MutableList<Depot>

    @CachePut(key = "#id")
    fun save(depot: Depot): Int

    @Query("""
        SELECT t1.*
        FROM crm_depot t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<Depot>

    @Query("""
        SELECT t1.*
        FROM crm_depot t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: MutableList<String>): MutableList<Depot>

    fun findByChainId(chainId: String): MutableList<Depot>

    @Query("""
        SELECT
            t1.*
        FROM
            crm_depot t1,
            crm_account_depot t2
        WHERE
            t1.enabled = 1
        AND t1.is_hidden = 0
        AND t1.id = t2.depot_id
        AND t2.account_id = ?1
    """, nativeQuery = true)
    fun findByAccountId(accountId: String): MutableList<Depot>

    @Query("""
        SELECT
            t1.*
        FROM
            crm_depot t1
        WHERE
            t1.enabled = 1
        AND t1.name IN ?1
    """, nativeQuery = true)
    fun findByNameList(nameList: MutableList<String>): MutableList<Depot>

    fun findByName(name: String): Depot

}

interface DepotRepositoryCustom{

    fun findShopList(depotShopQuery: DepotQuery): MutableList<DepotDto>

    fun findStoreList(depotShopQuery: DepotQuery): MutableList<DepotDto>

    fun findPage(pageable: Pageable, depotQuery: DepotQuery): Page<DepotDto>

    fun findDepotAccountList(pageable: Pageable,depotAccountQuery: DepotAccountQuery,boolean: Boolean):Page<DepotAccountDto>
}

@Suppress("UNCHECKED_CAST")
class DepotRepositoryImpl @Autowired constructor(val entityManager: EntityManager):DepotRepositoryCustom{

    override fun findShopList(depotShopQuery: DepotQuery): MutableList<DepotDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_depot t1,
                crm_depot_shop t2
            WHERE
                t1.depot_shop_id = t2.id
            AND t1.enabled = 1
            AND t2.enabled = 1
        """)
        if (depotShopQuery.clientIsNull != null) {
            if(depotShopQuery.clientIsNull){
                sb.append("""  and t1.client_id is NULL """)
            }else{
                sb.append("""  and t1.client_id is NOT NULL """)
            }
        }
        if (depotShopQuery.adShop != null) {
            if(depotShopQuery.adShop){
                sb.append("""  and t1.ad_shop is NULL """)
            }else{
                sb.append("""  and t1.ad_shop is NOT NULL """)
            }
        }
        if (depotShopQuery.popShop != null) {
            if(depotShopQuery.popShop){
                sb.append("""  and t1.pop_shop is NULL """)
            }else{
                sb.append("""  and t1.pop_shop is NOT NULL """)
            }
        }
        if (StringUtils.isNotEmpty(depotShopQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), DepotDto::class.java)

        return query.resultList as MutableList<DepotDto>
    }

    override fun findStoreList(depotStoreQuery: DepotQuery): MutableList<DepotDto>{
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_depot t1,
                crm_depot_store t2
            WHERE
                t1.depot_store_id = t2.id
            AND t1.enabled = 1
            AND t2.enabled = 1
        """)
        if (depotStoreQuery.outIdIsNull != null) {
            if(depotStoreQuery.outIdIsNull){
                sb.append("""  and t1.out_id is NULL """)
            }else{
                sb.append("""  and t1.out_id is NOT NULL """)
            }
        }
        if (StringUtils.isNotEmpty(depotStoreQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), DepotDto::class.java)

        return query.resultList as MutableList<DepotDto>
    }

    override fun findPage(pageable: Pageable, depotQuery: DepotQuery): Page<DepotDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_depot t1
            WHERE
                t1.enabled=1
        """)
        //TODO 查询条件
        var query = entityManager.createNativeQuery(sb.toString(), DepotDto::class.java)

        return query.resultList as Page<DepotDto>
    }

    override fun findDepotAccountList(pageable: Pageable,depotAccountQuery: DepotAccountQuery,boolean: Boolean):Page<DepotAccountDto>{
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.id,
                t1.name,
                t1.office_id,
                t1.client_id
            FROM
                crm_depot t1,
                crm_depot_shop t2
            WHERE
                t1.enabled = 1
            AND t1.is_hidden = 0
            AND t1.id = t2.depot_id
            AND t1.client_id IS NOT NULL
        """)
        //TODO 查询条件
        var query = entityManager.createNativeQuery(sb.toString(), DepotAccountDto::class.java)

        return query.resultList as Page<DepotAccountDto>
    }
}