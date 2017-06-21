package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Depot
import net.myspring.future.modules.basic.dto.DepotAccountDto
import net.myspring.future.modules.basic.dto.DepotDto
import net.myspring.future.modules.basic.web.query.DepotAccountQuery
import net.myspring.future.modules.basic.web.query.DepotQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import javax.persistence.EntityManager


@CacheConfig(cacheNames = arrayOf("depots"))
interface DepotRepository :BaseRepository<Depot,String>,DepotRepositoryCustom {
    @Cacheable
    override fun findOne(id: String): Depot
    @CachePut(key = "#p0.id")
    fun save(depot: Depot): Depot

    override fun findAll(): MutableList<Depot>

    fun findByEnabledIsTrueAndIdIn(idList: MutableList<String>): MutableList<Depot>

    fun findByEnabledIsTrueAndDepotShopIdIsNotNullAndCompanyId(companyId :String): MutableList<Depot>

    fun findByEnabledIsTrueAndAdShopIsTrueAndIsHiddenIsFalse():MutableList<Depot>

    fun findByChainId(chainId: String): MutableList<Depot>

    fun findByEnabledIsTrueAndNameIn(nameList: MutableList<String>): MutableList<Depot>

    fun findByName(name: String): Depot

    fun findByCompanyIdAndName(companyId: String, name: String): Depot

    @Query("""
        select t
        from #{#entityName} t
        where t.name in ?1
    """)
    fun findByNameList(nameList: MutableList<String>):MutableList<Depot>

}

interface DepotRepositoryCustom{

    fun findShopList(depotShopQuery: DepotQuery): MutableList<DepotDto>

    fun findStoreList(depotShopQuery: DepotQuery): MutableList<DepotDto>

    fun findPage(pageable: Pageable, depotQuery: DepotQuery): Page<DepotDto>

    fun findDepotAccountList(pageable: Pageable,depotAccountQuery: DepotAccountQuery):Page<DepotAccountDto>

    fun findByAccountId(accountId: String): MutableList<Depot>

    fun findByFilter(depotQuery: DepotQuery):MutableList<Depot>

    fun findAdStoreDtoList(companyId: String, outGroupId: String): List<DepotDto>

    fun findChainIds(depotQuery: DepotQuery):MutableList<String>
}

@Suppress("UNCHECKED_CAST")
class DepotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate, val entityManager: EntityManager):DepotRepositoryCustom{
    override fun findChainIds(depotQuery: DepotQuery): MutableList<String> {
        val sb = StringBuffer("""
            select
                distinct depot.chain_id
            from
                crm_depot depot
            where
                depot.enabled=1
        """)
        if(CollectionUtil.isNotEmpty(depotQuery.depotIdList)){
            sb.append("""  and depot.id in (:depotIdList) """)
        }
        if(CollectionUtil.isNotEmpty(depotQuery.officeIdList)){
            sb.append("""  and depot.office_id in (:officeIdList) """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(depotQuery), BeanPropertyRowMapper(String::class.java))
    }

    override fun findAdStoreDtoList(companyId: String, outGroupId: String): List<DepotDto> {
        val params = HashMap<String, Any>()
        params.put("companyId", companyId)
        params.put("outGroupId", outGroupId)

        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.*
            FROM
                crm_depot t1,
                crm_depot_store t2
            WHERE
                t1.depot_store_id = t2.id
            AND t1.enabled = 1
            AND t2.enabled = 1
            AND t1.company_id = :companyId
            AND t2.out_group_id = :outGroupId
            AND t1.is_hidden = 0
          """, params, BeanPropertyRowMapper(DepotDto::class.java))
    }

    override fun findByFilter(depotQuery: DepotQuery): MutableList<Depot> {
        val sb = StringBuffer("""
            SELECT
                t1.*
            FROM
                crm_depot t1
            WHERE
             t1.enabled = 1
        """)
        if(StringUtils.isNotBlank(depotQuery.name)){
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        if(StringUtils.isNotBlank(depotQuery.areaId)){
            sb.append("""  and t1.area_id=:areaId """)
        }
        if(StringUtils.isNotBlank(depotQuery.officeId)){
            sb.append("""  and t1.office_id=:officeId """)
        }
        if(CollectionUtil.isNotEmpty(depotQuery.depotIdList)){
            sb.append("""  and t1.id in (:depotIdList) """)
        }
        if(CollectionUtil.isNotEmpty(depotQuery.officeIdList)){
            sb.append("""  and t1.office_id in (:officeIdList) """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(depotQuery), BeanPropertyRowMapper(Depot::class.java))
    }

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
                sb.append("  and t1.client_id is NULL ")
            }else{
                sb.append("  and t1.client_id is NOT NULL ")
            }
        }
        if (depotShopQuery.adShop != null) {
            if(depotShopQuery.adShop){
                sb.append("  and t1.ad_shop is NULL ")
            }else{
                sb.append("  and t1.ad_shop is NOT NULL ")
            }
        }
        if (depotShopQuery.popShop != null) {
            if(depotShopQuery.popShop){
                sb.append("  and t1.pop_shop is NULL ")
            }else{
                sb.append("  and t1.pop_shop is NOT NULL ")
            }
        }
        if (StringUtils.isNotEmpty(depotShopQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        sb.append(" limit 0,20")
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
        var query = entityManager.createNativeQuery(sb.toString(), DepotDto::class.java)

        return query.resultList as Page<DepotDto>
    }

    override fun findDepotAccountList(pageable: Pageable,depotAccountQuery: DepotAccountQuery):Page<DepotAccountDto>{
        val sb = StringBuffer()
        sb.append("""
        select
        t1.id,
        t1.name,
        t1.office_id,
        t1.area_id,
        t1.client_id,
        scbzjDeposit.left_amount scbzj,
        xxbzjDeposit.left_amount xxbzj,
        ysjyjDeposit.left_amount ysjyj
        from
        crm_depot t1
        LEFT JOIN crm_depot_shop t2 ON t1.depot_shop_id = t2.id
        LEFT JOIN crm_shop_deposit scbzjDeposit ON scbzjDeposit.shop_id = t1.id AND scbzjDeposit.type='市场保证金' AND scbzjDeposit.locked = 0 AND scbzjDeposit.enabled = 1
        LEFT JOIN crm_shop_deposit xxbzjDeposit ON xxbzjDeposit.shop_id = t1.id AND xxbzjDeposit.type='形象保证金' AND xxbzjDeposit.locked = 0 AND xxbzjDeposit.enabled = 1
        LEFT JOIN crm_shop_deposit ysjyjDeposit ON ysjyjDeposit.shop_id = t1.id AND ysjyjDeposit.type='演示机押金' AND ysjyjDeposit.locked = 0 AND ysjyjDeposit.enabled = 1
        where
        t1.enabled =1
        and t1.is_hidden=0
        and t1.client_id IS NOT NULL
        """)
        if(depotAccountQuery.specialityStore != null && depotAccountQuery.specialityStore ){
            sb.append("""   and t2.speciality_store_type in ("总公司自营", "总公司经销商联营", "综合卖场") """)
        }
        if((depotAccountQuery.specialityStore == null || !depotAccountQuery.specialityStore) && depotAccountQuery.accountTaxPermitted != null && depotAccountQuery.accountTaxPermitted){
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

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(depotAccountQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(DepotAccountDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)
    }
}