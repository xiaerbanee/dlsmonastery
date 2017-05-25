package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ExpressOrder

import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import net.myspring.future.modules.crm.dto.PriceChangeDto
import net.myspring.future.modules.crm.web.query.PriceChangeQuery
import net.myspring.util.text.StringUtils
import org.apache.ibatis.annotations.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager


interface ExpressOrderRepository : BaseRepository<ExpressOrder, String> {


    fun findByExtendIdAndExtendType(extendId: String, extendType: String): ExpressOrder

    @Query("""
    SELECT
        t1.*
    FROM
        crm_express_order t1
    WHERE
        t1.enabled=1 and t1.id = ?1
        """, nativeQuery = true)
    fun findDto(id: String): ExpressOrderDto

    @Query("""
    SELECT
        t1.*
    FROM
        crm_express_order t1, crm_goods_order t2
    WHERE
        t1.enabled=1  and t1.id = t2.express_order_id
        and t2.id = ?1
        """, nativeQuery = true)
    fun findDtoByGoodsOrderId(goodsOrderId: String): ExpressOrderDto

}

interface ExpressOrderRepositoryCustom{
    fun findPage(@Param("p") priceChangeQuery : PriceChangeQuery): List<PriceChangeDto>
}

class ExpressOrderRepositoryImpl @Autowired constructor(val entityManager: EntityManager): ExpressOrderRepositoryCustom {
    override fun findPage(priceChangeQuery: PriceChangeQuery): List<PriceChangeDto> {
        val sb = StringBuffer()
        sb.append("""
         SELECT
            group_concat(DISTINCT productType.name) productTypeName, t1.*
        FROM
            crm_price_change t1,crm_price_change_product product,crm_product_type productType
        WHERE
            t1.enabled=1
            AND product.product_type_id = productType.id
            AND product.price_change_id = t1.id
        """)
        if (StringUtils.isNotEmpty(priceChangeQuery.name)) {
            sb.append("""
                AND t1.name LIKE CONCAT('%', :name, '%')
            """)
        }
        sb.append("""
                GROUP BY product.price_change_id
            """)
        val query = entityManager.createNativeQuery(sb.toString(), PriceChangeDto::class.java)
        query.setParameter("name", priceChangeQuery.name)

        return query.resultList as List<PriceChangeDto>

    }


}