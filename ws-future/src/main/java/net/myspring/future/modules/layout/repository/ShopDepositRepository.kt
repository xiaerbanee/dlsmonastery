package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopDeposit
import net.myspring.future.modules.layout.dto.ShopAllotDto
import net.myspring.future.modules.layout.dto.ShopDepositDto
import net.myspring.future.modules.layout.web.query.ShopDepositQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface ShopDepositRepository : BaseRepository<ShopDeposit,String>,ShopDepositRepositoryCustom {


}

interface ShopDepositRepositoryCustom{
    fun findPage(pageable: Pageable, shopDepositQuery: ShopDepositQuery): Page<ShopDepositDto>

    fun findLatest(type: String, shopId: String): ShopDeposit?

    fun findDto(id: String): ShopDepositDto

}

class ShopDepositRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ShopDepositRepositoryCustom{
    override fun findDto(id: String): ShopDepositDto {
        return namedParameterJdbcTemplate.queryForObject("""
        SELECT
            depot.office_id shopOfficeId,
            depot.area_id shopAreaId,
            t1.*
        FROM
            crm_shop_deposit t1,
            crm_depot depot
        WHERE
            t1.id = :id
            AND t1.shop_id = depot.id
            AND depot.enabled = 1
          """, Collections.singletonMap("id", id), BeanPropertyRowMapper(ShopDepositDto::class.java))

    }

    override fun findLatest(type: String, shopId: String): ShopDeposit? {

        val params = HashMap<String, Any>()
        params.put("type", type)
        params.put("shopId", shopId)

        val result = namedParameterJdbcTemplate.query("""
        SELECT
            t1.*
        FROM
            crm_shop_deposit t1
        WHERE
            t1.shop_id = :shopId
        AND t1.type = :type
        AND t1.enabled = 1
        ORDER BY
            t1.created_date DESC,
            t1.id DESC
        LIMIT  1
          """, params, BeanPropertyRowMapper(ShopDeposit::class.java))

        if(result !=null && result.size==1){
            return result[0]
        }else{
            return null
        }
    }

    override fun findPage(pageable: Pageable, shopDepositQuery: ShopDepositQuery): Page<ShopDepositDto> {

        val sb = StringBuffer()
        sb.append("""
        SELECT
            depot.office_id shopOfficeId,
            depot.area_id shopAreaId,
            t1.*
        FROM
            crm_shop_deposit t1,
            crm_depot depot
        WHERE
            t1.enabled = 1
            AND t1.shop_id = depot.id
            AND depot.enabled = 1
        """)
        if(StringUtils.isNotBlank(shopDepositQuery.shopName)){
            sb.append("""  AND depot.name LIKE CONCAT('%',:shopName,'%')  """)
        }
        if(StringUtils.isNotBlank(shopDepositQuery.type)){
            sb.append("""  AND t1.type =:type  """)
        }
        if(StringUtils.isNotBlank(shopDepositQuery.remarks)){
            sb.append("""  AND t1.remarks LIKE CONCAT('%', :remarks,'%')  """)
        }
        if(shopDepositQuery.createdDateStart != null){
            sb.append("""  AND t1.created_date >= :createdDateStart """)
        }
        if(shopDepositQuery.createdDateEnd != null){
            sb.append("""  AND t1.created_date < :createdDateEnd  """)
        }
        if(StringUtils.isNotBlank(shopDepositQuery.createdBy)){
            sb.append("""  AND t1.created_by = :createdBy """)
        }


        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(shopDepositQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ShopDepositDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)


    }
}