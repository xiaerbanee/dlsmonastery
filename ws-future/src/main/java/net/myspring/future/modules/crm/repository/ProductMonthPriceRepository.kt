package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ProductMonthPrice
import net.myspring.future.modules.crm.dto.ProductMonthPriceDetailDto
import net.myspring.future.modules.crm.dto.ProductMonthPriceDto
import net.myspring.future.modules.crm.dto.StoreAllotDto
import net.myspring.future.modules.crm.web.query.ProductMonthPriceQuery
import net.myspring.util.repository.MySQLDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface ProductMonthPriceRepository : BaseRepository<ProductMonthPrice, String> ,ProductMonthPriceRepositoryCustom{

    fun findByCompanyIdAndMonthAndEnabledIsTrue(companyId: String, month: String): ProductMonthPrice?


}

interface ProductMonthPriceRepositoryCustom {

    fun findPage(pageable: Pageable, productMonthPriceQuery: ProductMonthPriceQuery): Page<ProductMonthPriceDto>

    fun findDto(id: String): ProductMonthPriceDto

}


class ProductMonthPriceRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ProductMonthPriceRepositoryCustom{

    override fun findDto(id: String): ProductMonthPriceDto {
        return namedParameterJdbcTemplate.queryForObject("""
            SELECT
                t1.*
            FROM
                crm_product_month_price t1
            WHERE
                t1.id =  :id

                """, Collections.singletonMap("id", id), MyBeanPropertyRowMapper(ProductMonthPriceDto::class.java))
    }


    override fun findPage(pageable: Pageable, productMonthPriceQuery: ProductMonthPriceQuery): Page<ProductMonthPriceDto> {
        var sql = StringBuilder("""
                SELECT
                t1.*
                FROM
                crm_product_month_price t1
                WHERE
                t1.enabled=1
            """);
        if(productMonthPriceQuery.month!=null){
            sql.append("""
                    AND t1.month = :month
                """);
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sql.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sql.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(productMonthPriceQuery), BeanPropertyRowMapper(ProductMonthPriceDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(productMonthPriceQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }


}