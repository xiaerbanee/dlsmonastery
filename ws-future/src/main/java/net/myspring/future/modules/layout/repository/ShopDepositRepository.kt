package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopDeposit
import net.myspring.future.modules.layout.dto.ShopDepositDto
import net.myspring.future.modules.layout.web.query.ShopDepositQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopDepositRepository : BaseRepository<ShopDeposit,String>,ShopDepositRepositoryCustom {

    @Query("""
    SELECT
        t1.*
    FROM
        crm_shop_deposit t1
    WHERE
        t1.id IN (
            SELECT
                Max(id)
            FROM
                crm_shop_deposit
            WHERE
                `type` = :type
                AND shop_id IN :shopIds
            GROUP BY
                shop_id,
                type
        )
    """, nativeQuery = true)
    fun findByTypeAndShopIds(@Param("type") type: String, @Param("shopIds") shopIds: MutableList<String>): MutableList<ShopDeposit>

    @Query("""
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
    LIMIT 0,
     :size
    """, nativeQuery = true)
    fun findByTypeAndShopId(@Param("shopId") shopId: String, @Param("type") type: String, @Param("size") size: Int?): MutableList<ShopDeposit>
}

interface ShopDepositRepositoryCustom{
    fun findPage(pageable: Pageable, shopDepositQuery: ShopDepositQuery): Page<ShopDepositDto>
}

class ShopDepositRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ShopDepositRepositoryCustom{

    override fun findPage(pageable: Pageable, shopDepositQuery: ShopDepositQuery): Page<ShopDepositDto> {


        val sb = StringBuffer()
        sb.append("""
        SELECT
            depot.office_id shopOfficeId,
            depot.name shopName,
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


        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(shopDepositQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ShopDepositDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)


    }
}