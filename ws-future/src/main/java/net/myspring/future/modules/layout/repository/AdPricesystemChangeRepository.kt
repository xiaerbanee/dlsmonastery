package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.AdPricesystemChange
import net.myspring.future.modules.layout.dto.AdPricesystemChangeDto
import net.myspring.future.modules.layout.web.query.AdPricesystemChangeQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface AdPricesystemChangeRepository : BaseRepository<AdPricesystemChange,String>,AdPricesystemChangeRepositoryCustom{

}

interface AdPricesystemChangeRepositoryCustom{
    fun findPage(pageable: Pageable,adPricesystemChangeQuery: AdPricesystemChangeQuery): Page<AdPricesystemChangeDto>

    fun findFilter(adPricesystemChangeQuery: AdPricesystemChangeQuery): MutableList<AdPricesystemChangeDto>
}

class AdPricesystemChangeRepositoryImpl @Autowired constructor(val entityManager: EntityManager):AdPricesystemChangeRepositoryCustom{

    override fun findPage(pageable: Pageable,adPricesystemChangeQuery: AdPricesystemChangeQuery): Page<AdPricesystemChangeDto>{
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_ad_pricesystem_change t1
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(adPricesystemChangeQuery.productId)) {
            sb.append("""  and t1.product_id = :productId """)
        }

        var query = entityManager.createNativeQuery(sb.toString(),AdPricesystemChangeDto::class.java)

        return query.resultList as Page<AdPricesystemChangeDto>
    }

    override fun findFilter(adPricesystemChangeQuery: AdPricesystemChangeQuery): MutableList<AdPricesystemChangeDto>{
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_ad_pricesystem_change t1
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(adPricesystemChangeQuery.productId)) {
            sb.append("""  and t1.product_id = :productId """)
        }

        var query = entityManager.createNativeQuery(sb.toString(),AdPricesystemChangeDto::class.java)

        return query.resultList as MutableList<AdPricesystemChangeDto>
    }
}