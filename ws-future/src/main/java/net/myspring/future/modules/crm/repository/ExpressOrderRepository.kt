package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ExpressOrder

import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import net.myspring.future.modules.crm.dto.ExpressDto
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import net.myspring.future.modules.crm.dto.PriceChangeDto
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery
import net.myspring.future.modules.crm.web.query.PriceChangeQuery
import net.myspring.util.text.StringUtils
import org.springframework.data.repository.query.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import javax.persistence.EntityManager


interface ExpressOrderRepository : BaseRepository<ExpressOrder, String>, ExpressOrderRepositoryCustom {

    fun findByExtendIdAndExtendType(extendId: String, extendType: String): ExpressOrder

}

interface ExpressOrderRepositoryCustom{
    fun findPage(pageable : Pageable, expressOrderQuery : ExpressOrderQuery): Page<ExpressOrderDto>?

    fun findDtoByGoodsOrderId(goodsOrderId: String): ExpressOrderDto

    fun findDto(id: String): ExpressOrderDto
}

class ExpressOrderRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ExpressOrderRepositoryCustom {
    override fun findDto(id: String): ExpressOrderDto {
        return namedParameterJdbcTemplate.queryForObject("""
       SELECT
            t1.*
        FROM
            crm_express_order t1
        WHERE
            t1.enabled=1 and t1.id = :id
          """, Collections.singletonMap("id", id), MyBeanPropertyRowMapper(ExpressOrderDto::class.java))
    }

    override fun findDtoByGoodsOrderId(goodsOrderId: String): ExpressOrderDto {
        return namedParameterJdbcTemplate.queryForObject("""
        SELECT
            t1.*
        FROM
            crm_express_order t1, crm_goods_order t2
        WHERE
            t1.enabled=1  and t1.id = t2.express_order_id
            and t2.id = :goodsOrderId
          """, Collections.singletonMap("goodsOrderId", goodsOrderId), MyBeanPropertyRowMapper(ExpressOrderDto::class.java))
    }

    override fun findPage(pageable : Pageable,expressOrderQuery: ExpressOrderQuery): Page<ExpressOrderDto>? {

        return null

    }


}