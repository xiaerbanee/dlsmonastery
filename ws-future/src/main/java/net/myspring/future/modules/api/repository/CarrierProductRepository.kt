package net.myspring.future.modules.api.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.api.domain.CarrierProduct
import net.myspring.future.modules.api.dto.CarrierProductDto
import net.myspring.future.modules.api.dto.CarrierShopDto
import net.myspring.future.modules.api.web.query.CarrierProductQuery
import net.myspring.future.modules.api.web.query.CarrierShopQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface CarrierProductRepository : BaseRepository<CarrierProduct, String>, CarrierProductRepositoryCustom {

    fun findByName(name:String): CarrierProduct

    fun findByProductIdIn(productIdList:MutableList<String>):MutableList<CarrierProduct>
}


interface CarrierProductRepositoryCustom {
    fun findPage(pageable: Pageable, carrierProductQuery: CarrierProductQuery): Page<CarrierProductDto>
}

class CarrierProductRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : CarrierProductRepositoryCustom {
    override fun findPage(pageable: Pageable, carrierProductQuery: CarrierProductQuery): Page<CarrierProductDto> {
        val sb = StringBuffer()
        sb.append("""
         SELECT
            t1.*
        FROM
            api_carrier_product t1
        WHERE
            t1.enabled = 1
        """)
        if (StringUtils.isNotBlank(carrierProductQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%')   """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(), pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(carrierProductQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql, paramMap, BeanPropertyRowMapper(CarrierProductDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list, pageable, count)
    }

}