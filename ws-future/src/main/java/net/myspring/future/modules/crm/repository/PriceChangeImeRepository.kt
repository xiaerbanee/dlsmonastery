package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.*

import net.myspring.future.modules.crm.dto.GoodsOrderDto
import net.myspring.future.modules.crm.dto.PriceChangeImeDto
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.data.repository.query.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityManager


interface PriceChangeImeRepository : BaseRepository<PriceChangeIme, String>,PriceChangeImeRepositoryCustom {

    fun findByPriceChangeId(priceChangeId: String): MutableList<PriceChangeIme>

    @Query("""
      select t
      from #{#entityName} t
      where t.enabled = 1
      and t.priceChangeId = :priceChangeId
      and t.saleDate < :maxDateTime
      and t.uploadDate is null
    """)
    fun findCheckList(@Param("priceChangeId")priceChangeId: String,@Param("maxDateTime")maxDateTime:LocalDate):MutableList<PriceChangeIme>

    fun findByPriceChangeIdAndUploadDateIsNotNullAndEnabledIsTrue(priceChangeId: String): MutableList<PriceChangeIme>




}


interface PriceChangeImeRepositoryCustom{

    fun countByPriceChangeId(priceChangeId:String):Int

    fun findPage(pageable : Pageable, priceChangeImeQuery : PriceChangeImeQuery): Page<PriceChangeImeDto>

}

class PriceChangeImeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): PriceChangeImeRepositoryCustom {

    override fun countByPriceChangeId(priceChangeId:String):Int{
        return namedParameterJdbcTemplate.queryForObject("""
            SELECT
	            COUNT(*)
            FROM
	            crm_price_change_ime t
            WHERE
	            t.enabled = 1
            AND t.price_change_id = :priceChangeId
        """,Collections.singletonMap("priceChangeId",priceChangeId),Int::class.java)
    }

    override fun findPage(pageable : Pageable, priceChangeImeQuery: PriceChangeImeQuery): Page<PriceChangeImeDto> {

        val sb = StringBuilder("""
        SELECT
            depot.office_id officeId,
            t2. NAME priceChangeName,
            productIme.ime ime,
            productIme.product_id productId,
            t1.*
        FROM
            crm_price_change_ime t1,
            crm_price_change t2,
            crm_depot depot,
            crm_product_ime productIme
        WHERE
            t1.price_change_id = t2.id
        AND t1.shop_id = depot.id
        AND t1.product_ime_id = productIme.id
        AND t1.enabled = 1
        """)
        if(StringUtils.isNotBlank(priceChangeImeQuery.status)){
            sb.append("""  and t1.status=:status  """)
        }
        if(priceChangeImeQuery.isCheck != null){
            if(priceChangeImeQuery.isCheck == true){
                sb.append("""  and t1.is_check = 1  """)
            }else{
                sb.append("""  and t1.is_check = 0  """)
            }

        }
        if(priceChangeImeQuery.hasImage != null){
            if(priceChangeImeQuery.hasImage == true){
                sb.append(""" and  t1.image is not null  """)
            }else{
                sb.append(""" and t1.image is null  """)
            }
        }
        if(StringUtils.isNotBlank(priceChangeImeQuery.priceChangeName)){
            sb.append("""  and t2.name like concat('%',:priceChangeName, '%')  """)
        }
        if(StringUtils.isNotBlank(priceChangeImeQuery.productId)){
            sb.append("""  and productIme.product_id=:productId  """)
        }
        if(StringUtils.isNotBlank(priceChangeImeQuery.shopId)){
            sb.append("""  and t1.shop_id=:shopId  """)
        }
        if(StringUtils.isNotBlank(priceChangeImeQuery.ime)){
            sb.append("""  and productIme.ime like concat('%',:ime, '%')  """)
        }
        if(StringUtils.isNotBlank(priceChangeImeQuery.officeId)){
            sb.append("""  and depot.office_id=:officeId  """)
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(priceChangeImeQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(PriceChangeImeDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)
    }


}