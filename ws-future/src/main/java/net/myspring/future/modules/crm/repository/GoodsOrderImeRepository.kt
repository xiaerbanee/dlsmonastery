package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository

import java.time.LocalDate

import net.myspring.future.modules.crm.domain.GoodsOrder
import net.myspring.future.modules.crm.domain.GoodsOrderIme
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto
import net.myspring.future.modules.crm.dto.StoreAllotDto
import net.myspring.future.modules.crm.dto.StoreAllotImeDto
import net.myspring.future.modules.crm.web.query.StoreAllotQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface GoodsOrderImeRepository : BaseRepository<GoodsOrderIme, String>, GoodsOrderImeRepositoryCustom {

    fun findByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderIme>

}


interface GoodsOrderImeRepositoryCustom{

    fun findDtoListByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderImeDto>

}

class GoodsOrderImeRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): GoodsOrderImeRepositoryCustom{
    override fun findDtoListByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderImeDto> {
        return namedParameterJdbcTemplate.query("""
        SELECT
            t2.ime productImeIme,
            t2.meid productImeMeid,
            t1.*
        FROM
            crm_goods_order_ime t1,
            crm_product_ime t2
        WHERE
            t1.goods_order_id = :goodsOrderId
        AND t1.enabled = 1
        AND t1.product_ime_id = t2.id
          """, Collections.singletonMap("goodsOrderId", goodsOrderId), MyBeanPropertyRowMapper(GoodsOrderImeDto::class.java))
    }


}