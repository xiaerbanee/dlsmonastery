package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ProductMonthPriceDetail
import net.myspring.future.modules.crm.dto.ProductMonthPriceDetailDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface ProductMonthPriceDetailRepository : BaseRepository<ProductMonthPriceDetail, String>, ProductMonthPriceDetailRepositoryCustom {

}


interface ProductMonthPriceDetailRepositoryCustom {

    fun findDetailListForNew(): List<ProductMonthPriceDetailDto>

    fun findDetailListForEdit( productMonthPriceId :String): List<ProductMonthPriceDetailDto>

}


class ProductMonthPriceDetailRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ProductMonthPriceDetailRepositoryCustom{
    override fun findDetailListForEdit(productMonthPriceId: String): List<ProductMonthPriceDetailDto> {
        val paramMap = HashMap<String, Any>()
        paramMap.put("productMonthPriceId",productMonthPriceId)

        return namedParameterJdbcTemplate.query("""
       SELECT
            result.id,
            result.productTypeId,
            result.baokaPrice,
            result.price2,
            result.price3
        FROM
            (
                (
                    SELECT
                        t1.id id,
                        t1.product_type_id productTypeId,
                        t1.baoka_price baokaPrice,
                        t1.price2 price2,
                        t1.price3 price3
                    FROM
                        crm_product_month_price_detail t1
                    WHERE
                        t1.product_month_price_id = :productMonthPriceId
                )
                UNION ALL
                    (
                        SELECT
                            NULL id,
                            t1.id productTypeId,
                            t1.baoka_price baokaPrice,
                            t1.price2 price2,
                            t1.price3 price3
                        FROM
                            crm_product_type t1
                        WHERE
                            t1.enabled = 1
                            AND t1.score_type = 1
                            AND t1.price1 IS NOT NULL
                            AND NOT EXISTS (
                                SELECT  *
                                FROM crm_product_month_price_detail detail
                                WHERE detail.product_month_price_id = :productMonthPriceId AND detail.product_type_id = t1.id
                            )
                    )
            ) result
        ORDER BY
            result.id DESC
          """, paramMap, BeanPropertyRowMapper(ProductMonthPriceDetailDto::class.java))
    }

    override fun findDetailListForNew(): List<ProductMonthPriceDetailDto> {
        return namedParameterJdbcTemplate.query("""
         SELECT
            NULL id,
            t1.id productTypeId,
            t1.baoka_price,
            t1.price2,
            t1.price3
        FROM
            crm_product_type t1
        WHERE
            t1.enabled = 1
            AND t1.score_type = 1
            AND t1.price1 IS NOT NULL
          """,BeanPropertyRowMapper(ProductMonthPriceDetailDto::class.java))
    }


}