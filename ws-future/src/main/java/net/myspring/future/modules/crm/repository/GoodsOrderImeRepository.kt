package net.myspring.future.modules.crm.repository

import com.google.common.collect.Lists
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.GoodsOrderIme
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery
import net.myspring.util.collection.CollectionUtil
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

    fun findDtoListByGoodsOrderIdList(goodsOrderIdList: List<String>): MutableList<GoodsOrderImeDto>

}

class GoodsOrderImeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): GoodsOrderImeRepositoryCustom{

    override fun findDtoListByGoodsOrderIdList(goodsOrderIdList: List<String>): MutableList<GoodsOrderImeDto> {
        if(CollectionUtil.isEmpty(goodsOrderIdList)){
            return ArrayList()
        }
        return namedParameterJdbcTemplate.query("""
        SELECT
            t2.ime productImeIme,
            t2.ime2 productImeIme2,
            t2.meid productImeMeid,
            t3.name productName,
            t1.*
        FROM
            crm_goods_order_ime t1
            LEFT JOIN crm_product_ime t2 ON t1.product_ime_id = t2.id
            LEFT JOIN crm_product t3 ON t1.product_id = t3.id
        WHERE
            t1.goods_order_id IN (:goodsOrderIdList)
            AND t1.enabled = 1

          """, Collections.singletonMap("goodsOrderIdList", goodsOrderIdList), BeanPropertyRowMapper(GoodsOrderImeDto::class.java))
    }

    override fun findDtoListByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderImeDto> {
        return findDtoListByGoodsOrderIdList(Lists.newArrayList(goodsOrderId))
    }


}