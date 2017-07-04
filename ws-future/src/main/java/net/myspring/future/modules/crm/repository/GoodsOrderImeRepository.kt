package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.GoodsOrderIme
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface GoodsOrderImeRepository : BaseRepository<GoodsOrderIme, String>, GoodsOrderImeRepositoryCustom {

    fun findByEnabledIsTrueAndGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderIme>

    fun findByGoodsOrderIdIn(goodsOrderIdList: MutableList<String>): MutableList<GoodsOrderIme>

}


interface GoodsOrderImeRepositoryCustom{
    fun findDtoListByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderImeDto>

}

class GoodsOrderImeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): GoodsOrderImeRepositoryCustom{

    override fun findDtoListByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderImeDto> {
        return namedParameterJdbcTemplate.query("""
        SELECT
            t2.ime productImeIme,
            t2.meid productImeMeid,
            t3.name productName,
            t1.*
        FROM
            crm_goods_order_ime t1
            LEFT JOIN crm_product_ime t2 ON t1.product_ime_id = t2.id
            LEFT JOIN crm_product t3 ON t1.product_id = t3.id
        WHERE
            t1.goods_order_id = :goodsOrderId
            AND t1.enabled = 1

          """, Collections.singletonMap("goodsOrderId", goodsOrderId), BeanPropertyRowMapper(GoodsOrderImeDto::class.java))
    }


}