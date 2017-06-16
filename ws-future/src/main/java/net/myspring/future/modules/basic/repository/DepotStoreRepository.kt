package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DepotStore
import net.myspring.future.modules.basic.domain.ProductType
import net.myspring.future.modules.basic.dto.DepotReportDto
import net.myspring.future.modules.basic.dto.DepotShopDto
import net.myspring.future.modules.basic.dto.DepotStoreDto
import net.myspring.future.modules.basic.web.query.DepotStoreQuery
import net.myspring.future.modules.crm.web.query.ReportQuery
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
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("depotStores"))
interface DepotStoreRepository : BaseRepository<DepotStore,String>,DepotStoreRepositoryCustom{

    @Cacheable
    override fun findOne(id: String): DepotStore

    override fun findAll(): MutableList<DepotStore>

    @CachePut(key = "#p0.id")
    fun save(depotStore: DepotStore): DepotStore

    @Query("""
        SELECT t1.*
        FROM crm_depot_store t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<DepotStore>

    @Query("""
        SELECT t1.*
        FROM crm_depot_store t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: MutableList<String>): MutableList<DepotStore>

}

interface DepotStoreRepositoryCustom{
    fun findPage(pageable: Pageable, depotStoreQuery: DepotStoreQuery): Page<DepotStoreDto>

    fun findStoreReport(reportQuery:ReportQuery):MutableList<DepotReportDto>
}

class DepotStoreRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):DepotStoreRepositoryCustom{
    override fun findStoreReport(reportQuery: ReportQuery): MutableList<DepotReportDto> {
        val sb = StringBuffer()
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append("""   SELECT t4.id as depotId,t4.name as 'depotName', COUNT(t1.id) AS qty""")
        }else if(reportQuery.isDetail){
            sb.append("""
               SELECT t2.id as 'productId',t2.name as 'productName',t1.ime,t4.name as 'depotName'
            """)
        }
        sb.append("""
                    FROM
                    crm_product_ime t1
                    LEFT JOIN crm_product t2 on t1.product_id=t2.id
                    LEFT JOIN crm_product_type t3 on t2.product_type_id=t3.id
                    LEFT JOIN crm_depot t4 on t1.depot_id=t4.id,
                    crm_depot_store t5
                    WHERE
                    t1.enabled = 1
                    and  t4.depot_store_id=t5.id
        """)
        if(reportQuery.scoreType){
            sb.append("""  and t3.score_type =:scoreType """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.productTypeIdList)) {
            sb.append(""" and t3.id in (:productTypeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.officeIdList)) {
            sb.append(""" and t4.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.depotIdList)) {
            sb.append(""" and t4.id in (:depotIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.depotIds)) {
            sb.append(""" and t4.id in (:depotIds) """)
        }
        if (StringUtils.isNotBlank(reportQuery.depotId)) {
            sb.append(""" and t4.id=:depotId """)
        }
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append(""" group by t1.depot_id""")
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(reportQuery), BeanPropertyRowMapper(DepotReportDto::class.java))
    }

    override fun findPage(pageable: Pageable, depotStoreQuery: DepotStoreQuery): Page<DepotStoreDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t2.*, t1.id AS 'depotId',
                t1.name as 'depotName',
                t1.office_id AS 'officeId',
                t1.tax_name AS 'taxName',
                t1.delegate_depot_id AS 'delegateDepotId',
                depot.name as 'delegateDepotName',
                t1.pop_shop AS 'popShop'
            FROM
                crm_depot t1 left join crm_depot depot on t1.delegate_depot_id=depot.id,
                crm_depot_store t2
            WHERE
                t1.enabled = 1
            AND t2.enabled = 1
            AND t2.depot_id = t1.id
        """)
        if (StringUtils.isNotEmpty(depotStoreQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(depotStoreQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(DepotStoreDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)
    }
}