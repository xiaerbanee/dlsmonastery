package net.myspring.future.modules.layout.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit
import net.myspring.future.modules.layout.dto.ShopGoodsDepositDto
import net.myspring.future.modules.layout.web.query.ShopGoodsDepositQuery
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

/**
 * Created by zhangyf on 2017/5/24.
 */
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
}

class ShopGoodsDepositRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ShopGoodsDepositRepositoryCustom{

    override fun findPage(pageable: Pageable, shopGoodsDepositQuery: ShopGoodsDepositQuery): Page<ShopGoodsDepositDto> {


        val sb = StringBuffer()
        sb.append("""
        select  depot.office_id depotOfficeId, t1.*
        from crm_shop_goods_deposit t1, crm_depot depot
        where t1.enabled = 1
        and t1.shop_id = depot.id
        """)
        if(StringUtils.isNotBlank(shopGoodsDepositQuery.remarks)){
            sb.append("""  and t1.out_code like concat('%',:remarks,'%')  """)
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
            sb.append("""  and depot.name  like concat('%', :shopName,'%')  """)
        }
        if(StringUtils.isNotBlank(shopGoodsDepositQuery.bankName)){
            sb.append("""
                and t1.bank_id in(
                    select bank.id
                    from crm_bank bank
                    where bank.name like concat('%',:bankName,'%')
                )
           """)
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap =BeanPropertySqlParameterSource(shopGoodsDepositQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ShopGoodsDepositDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)


    }
}