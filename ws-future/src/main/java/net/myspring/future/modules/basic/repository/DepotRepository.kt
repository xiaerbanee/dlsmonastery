package net.myspring.future.modules.basic.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Depot
import net.myspring.future.modules.basic.dto.DepotAccountDto
import net.myspring.future.modules.basic.dto.DepotDto
import net.myspring.future.modules.basic.web.query.DepotAccountQuery
import net.myspring.future.modules.basic.web.query.DepotQuery
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.dto.StoreAllotDto
import net.myspring.future.modules.layout.dto.ShopDepositDto
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("depots"))
interface DepotRepository :BaseRepository<Depot,String>,DepotRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): Depot

    override fun findAll(): MutableList<Depot>

    @CachePut(key = "#p0.id")
    fun save(depot: Depot): Depot



    fun findByEnabledIsTrueAndIdIn(idList: MutableList<String>): MutableList<Depot>

    fun findByChainId(chainId: String): MutableList<Depot>

    fun findByEnabledIsTrueAndNameIn(nameList: MutableList<String>): MutableList<Depot>

    fun findByName(name: String): Depot

}

interface DepotRepositoryCustom{

    fun findShopList(depotShopQuery: DepotQuery): MutableList<DepotDto>

    fun findStoreList(depotShopQuery: DepotQuery): MutableList<DepotDto>

    fun findPage(pageable: Pageable, depotQuery: DepotQuery): Page<DepotDto>

    fun findDepotAccountList(pageable: Pageable,depotAccountQuery: DepotAccountQuery,accountTaxPermitted: Boolean):Page<DepotAccountDto>

    fun findByAccountId(accountId: String): MutableList<Depot>
}

@Suppress("UNCHECKED_CAST")
class DepotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate, val entityManager: EntityManager):DepotRepositoryCustom{
    override fun findByAccountId(accountId: String): MutableList<Depot> {
        return namedParameterJdbcTemplate.query("""
        SELECT
            t1.*
        FROM
            crm_depot t1,
            crm_account_depot t2
        WHERE
            t1.enabled = 1
        AND t1.is_hidden = 0
        AND t1.id = t2.depot_id
        AND t2.account_id = :accountId
          """, Collections.singletonMap("accountId", accountId), BeanPropertyRowMapper(Depot::class.java))
    }

    override fun findShopList(depotShopQuery: DepotQuery): MutableList<DepotDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_depot t1,
                crm_depot_shop t2
            WHERE
                t1.depot_shop_id = t2.id
            AND t1.enabled = 1
            AND t2.enabled = 1
        """)
        if (depotShopQuery.clientIsNull != null) {
            if(depotShopQuery.clientIsNull){
                sb.append("""  and t1.client_id is NULL """)
            }else{
                sb.append("""  and t1.client_id is NOT NULL """)
            }
        }
        if (depotShopQuery.adShop != null) {
            if(depotShopQuery.adShop){
                sb.append("""  and t1.ad_shop is NULL """)
            }else{
                sb.append("""  and t1.ad_shop is NOT NULL """)
            }
        }
        if (depotShopQuery.popShop != null) {
            if(depotShopQuery.popShop){
                sb.append("""  and t1.pop_shop is NULL """)
            }else{
                sb.append("""  and t1.pop_shop is NOT NULL """)
            }
        }
        if (StringUtils.isNotEmpty(depotShopQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }


        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(depotShopQuery), BeanPropertyRowMapper(DepotDto::class.java))

    }

    override fun findStoreList(depotStoreQuery: DepotQuery): MutableList<DepotDto>{
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_depot t1,
                crm_depot_store t2
            WHERE
                t1.depot_store_id = t2.id
            AND t1.enabled = 1
            AND t2.enabled = 1
        """)
        if (depotStoreQuery.outIdIsNull != null) {
            if(depotStoreQuery.outIdIsNull){
                sb.append("""  and t1.out_id is NULL """)
            }else{
                sb.append("""  and t1.out_id is NOT NULL """)
            }
        }
        if (StringUtils.isNotEmpty(depotStoreQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }


        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(depotStoreQuery), BeanPropertyRowMapper(DepotDto::class.java))

    }

    override fun findPage(pageable: Pageable, depotQuery: DepotQuery): Page<DepotDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_depot t1
            WHERE
                t1.enabled=1
        """)
        //TODO 查询条件
        var query = entityManager.createNativeQuery(sb.toString(), DepotDto::class.java)

        return query.resultList as Page<DepotDto>
    }

    override fun findDepotAccountList(pageable: Pageable,depotAccountQuery: DepotAccountQuery,accountTaxPermitted: Boolean):Page<DepotAccountDto>{

        val sb = StringBuffer()
        sb.append("""
        select
        t1.id, t1.name, t1.office_id, t1.client_id
        from
        crm_depot t1 , crm_depot_shop t2
        where
        t1.enabled =1
        and t1.is_hidden=0
        and t1.id = t2.depot_id
        and t1.client_id IS NOT NULL
        """)
        if(depotAccountQuery.specialityStore != null && depotAccountQuery.specialityStore ){
            sb.append("""   and t2.speciality_store_type in ("总公司自营", "总公司经销商联营", "综合卖场") """)
        }
        if((depotAccountQuery.specialityStore == null || !depotAccountQuery.specialityStore) && accountTaxPermitted){
            sb.append("""
                and t1.tax_name IS NOT NULL
                and t1.tax_name != ''
            """)
        }
        if(StringUtils.isNotBlank(depotAccountQuery.name)){
            sb.append("""  and t1.name like concat('%', :name,'%') """)
        }
        if(StringUtils.isNotBlank(depotAccountQuery.officeId)){
            sb.append("""  and t1.office_id = :officeId  """)
        }


        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(depotAccountQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(DepotAccountDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)
    }
}