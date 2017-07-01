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

    fun findByGoodsOrderIdIn(goodsOrderIdList: MutableList<String>): MutableList<GoodsOrderIme>

}


interface GoodsOrderImeRepositoryCustom{

}

class GoodsOrderImeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): GoodsOrderImeRepositoryCustom{

}