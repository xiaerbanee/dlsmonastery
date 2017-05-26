package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.ShopAdType
import net.myspring.future.modules.basic.dto.ShopAdTypeDto
import net.myspring.future.modules.basic.web.query.ShopAdTypeQuery
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
@CacheConfig(cacheNames = arrayOf("shopAdTypes"))
interface ShopAdTypeRepository : BaseRepository<ShopAdType,String>,ShopAdTypeRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): ShopAdType

    override fun findAll(): MutableList<ShopAdType>

    @CachePut(key = "#id")
    fun save(shopAdType: ShopAdType): Int

    @Query("""
        SELECT t1.*
        FROM crm_shop_ad_type t1
        where t1.enabled=1
    """, nativeQuery = true)
    //TODO 需要修改
    fun findAllByEnabled(): MutableList<ShopAdTypeDto>


}

interface ShopAdTypeRepositoryCustom{
    fun findPage(pageable: Pageable, shopAdTypeQuery: ShopAdTypeQuery): Page<ShopAdTypeDto>
}

class ShopAdTypeRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ShopAdTypeRepositoryCustom{

    override fun findPage(pageable: Pageable, shopAdTypeQuery: ShopAdTypeQuery): Page<ShopAdTypeDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_shop_ad_type t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(shopAdTypeQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        if (StringUtils.isNotEmpty(shopAdTypeQuery.totalPriceType)) {
            sb.append("""  and t1.total_price_type = :totalPriceType """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), ShopAdTypeDto::class.java)

        return query.resultList as Page<ShopAdTypeDto>
    }
}
