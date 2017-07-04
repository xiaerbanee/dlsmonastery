package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit
import net.myspring.future.modules.layout.dto.ShopGoodsDepositDto
import net.myspring.future.modules.layout.dto.ShopGoodsDepositSumDto
import net.myspring.future.modules.layout.web.query.ShopGoodsDepositQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface ShopGoodsDepositRepository : BaseRepository<ShopGoodsDeposit,String>,ShopGoodsDepositRepositoryCustom {

    @Query("""
    SELECT
        t1
    FROM
        #{#entityName} t1
    WHERE
        t1.enabled = 1
    AND t1.shopId = :shopId
    AND t1.status = :status
    ORDER BY
        created_date DESC
    """)
    fun findByShopId(shopId: String,status: String): MutableList<ShopGoodsDeposit>
}

interface ShopGoodsDepositRepositoryCustom{
    fun findPage(pageable: Pageable, shopGoodsDepositQuery: ShopGoodsDepositQuery): Page<ShopGoodsDepositDto>


    fun findShopGoodsDepositSumDtoList(): List<ShopGoodsDepositSumDto>

    fun findDto(id: String): ShopGoodsDepositDto

}

class ShopGoodsDepositRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ShopGoodsDepositRepositoryCustom{


    override fun findDto(id: String): ShopGoodsDepositDto {

        return namedParameterJdbcTemplate.queryForObject("""
        SELECT
            shop.office_id shopOfficeId,
            shop.area_id shopAreaId,
            client.out_id shopClientOutId,
            t1.*
        FROM
            crm_shop_goods_deposit t1
            LEFT JOIN crm_depot shop ON t1.shop_id = shop.id
            LEFT JOIN crm_client client ON shop.client_id = client.id
        WHERE
            t1.id = :id
          """, Collections.singletonMap("id", id), BeanPropertyRowMapper(ShopGoodsDepositDto::class.java))
    }

    override fun findShopGoodsDepositSumDtoList(): List<ShopGoodsDepositSumDto> {

        return namedParameterJdbcTemplate.query("""
       SELECT
            t2.id shopId,
            t2.area_id shopAreaId,
            sum(t1.amount) totalAmount
        FROM
            crm_shop_goods_deposit t1,
            crm_depot t2
        WHERE
            t1.enabled = 1
            AND t1.status='已通过'
            AND t1.shop_id = t2.id
        GROUP BY
            t2.id,  t2.area_id
          """, BeanPropertyRowMapper(ShopGoodsDepositSumDto::class.java))
    }

    override fun findPage(pageable: Pageable, shopGoodsDepositQuery: ShopGoodsDepositQuery): Page<ShopGoodsDepositDto> {

        val sb = StringBuffer()
        sb.append("""
        SELECT
            shop.office_id shopOfficeId,
            shop.area_id shopAreaId,
            client.out_id shopClientOutId,
            t1.*
        FROM
            crm_shop_goods_deposit t1
            LEFT JOIN crm_depot shop ON t1.shop_id = shop.id
            LEFT JOIN crm_client client ON shop.client_id = client.id
            LEFT JOIN crm_bank bank ON t1.bank_id = bank.id
        WHERE
            t1.enabled = 1
            AND t1.company_id = :companyId
        """)
        if(StringUtils.isNotBlank(shopGoodsDepositQuery.remarks)){
            sb.append("""  and t1.remarks like concat('%',:remarks,'%')  """)
        }
        if(StringUtils.isNotBlank(shopGoodsDepositQuery.status)){
            sb.append("""  and t1.status=:status  """)
        }
        if(shopGoodsDepositQuery.createdDateStart!=null){
            sb.append("""  AND t1.created_date >= :createdDateStart  """)
        }
        if(shopGoodsDepositQuery.createdDateEnd!=null){
            sb.append("""  AND t1.created_date < :createdDateEnd  """)
        }
        if(shopGoodsDepositQuery.amount!=null){
            sb.append("""   and t1.amount= :amount """)
        }
        if(StringUtils.isNotBlank(shopGoodsDepositQuery.outBillType)){
            sb.append("""  and t1.out_bill_type = :outBillType  """)
        }
        if(StringUtils.isNotBlank(shopGoodsDepositQuery.shopName)){
            sb.append("""  and shop.name  like concat('%', :shopName,'%')  """)
        }
        if(StringUtils.isNotBlank(shopGoodsDepositQuery.bankName)){
            sb.append("""  and bank.name like concat('%',:bankName,'%')    """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap =BeanPropertySqlParameterSource(shopGoodsDepositQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ShopGoodsDepositDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)


    }
}