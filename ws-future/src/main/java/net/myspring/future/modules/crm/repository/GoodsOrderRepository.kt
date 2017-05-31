package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.GoodsOrder
import net.myspring.future.modules.crm.dto.GoodsOrderDto
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate


interface GoodsOrderRepository : BaseRepository<GoodsOrder, String>, GoodsOrderRepositoryCustom {

    @Query("""
        SELECT
            MAX(t1.businessId)
        FROM
            #{#entityName} t1
        WHERE
            t1.billDate>=?1
        """)
    fun findMaxBusinessId(date: LocalDate): String
}

interface GoodsOrderRepositoryCustom {
    fun findAll(pageable: Pageable, goodsOrderQuery: GoodsOrderQuery): Page<GoodsOrderDto>?

}

class GoodsOrderRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : GoodsOrderRepositoryCustom {
    override fun findAll(pageable: Pageable, goodsOrderQuery: GoodsOrderQuery): Page<GoodsOrderDto>? {
        var sb = StringBuilder("select * from crm_goods_order where 1=1 ");
        if(StringUtils.isNotBlank(goodsOrderQuery.remarks)) {
            sb.append(" and remarks like concat('%',:remarks,'%')");
        }
        if(goodsOrderQuery.createdDateStart != null) {
            sb.append(" and created_date > :createdDateStart ");
        }
        if(goodsOrderQuery.createdDateEnd != null) {
            sb.append(" and created_date < :createdDateEnd ");
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(goodsOrderQuery), BeanPropertyRowMapper(GoodsOrderDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(goodsOrderQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}