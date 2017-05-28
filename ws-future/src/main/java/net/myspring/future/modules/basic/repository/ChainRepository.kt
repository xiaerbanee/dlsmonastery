package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Chain
import net.myspring.future.modules.basic.dto.ChainDto
import net.myspring.future.modules.basic.web.query.ChainQuery
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
@CacheConfig(cacheNames = arrayOf("chains"))
interface ChainRepository : BaseRepository<Chain,String>,ChainRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): Chain

    override fun findAll(): MutableList<Chain>

    @CachePut(key = "#p0.id")
    fun save(chain: Chain): Int

    @Query("""
        SELECT t1.*
        FROM crm_chain t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<Chain>

    @Query("""
        SELECT t1.*
        FROM crm_chain t1
        WHERE t1.id IN ?1
    """, nativeQuery = true)
    fun findByIds(ids: MutableList<String>): MutableList<Chain>

    @Query("""
        select id from crm_depot where chain_id=?1
    """, nativeQuery = true)
    fun findDepotIds(id: String): MutableList<String>
}

interface ChainRepositoryCustom{
    fun findPage(pageable: Pageable, chainQuery: ChainQuery): Page<ChainDto>
}

class ChainRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ChainRepositoryCustom{

    override fun findPage(pageable: Pageable, chainQuery: ChainQuery): Page<ChainDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_chain t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(chainQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), ChainDto::class.java)

        return query.resultList as Page<ChainDto>
    }
}

