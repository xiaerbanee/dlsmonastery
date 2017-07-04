package net.myspring.future.modules.crm.repository

import com.google.common.collect.Maps
import net.myspring.future.common.enums.AfterSaleTypeEnum
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSale
import net.myspring.future.modules.crm.dto.AfterSaleDto
import net.myspring.future.modules.crm.dto.GoodsOrderDto
import net.myspring.future.modules.crm.web.query.AfterSaleQuery
import net.myspring.util.collection.CollectionUtil
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
import java.time.LocalDate
import java.util.*


interface AfterSaleRepository : BaseRepository<AfterSale, String>,AfterSaleRepositoryCustom {

    @Query("""
    SELECT t1
    FROM #{#entityName} t1, ProductIme t2
    WHERE t1.badProductImeId=t2.id
        AND t2.ime in ?1
        """)
    fun findByBadProductImeIn(imeList: MutableList<String>): MutableList<AfterSale>

    fun findByBadProductImeIdIn(badProductImeId:MutableList<String>):MutableList<AfterSale>

}


interface AfterSaleRepositoryCustom{

    fun findPage(pageable: Pageable, afterSaleQuery : AfterSaleQuery): Page<AfterSaleDto>

    fun findFilter(afterSaleQuery : AfterSaleQuery): MutableList<AfterSaleDto>

    fun findDtoByImeIn(imeList:MutableList<String>): MutableList<AfterSaleDto>

    fun findDtoByIds(ids: MutableList<String>): MutableList<AfterSaleDto>

    @Query("""
    SELECT MAX(t1.businessId)
    FROM #{#entityName} t1
    WHERE t1.createdDate >= ?1
        """)
    fun findMaxBusinessId(dateStart: LocalDate): MutableList<String>

}

class AfterSaleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AfterSaleRepositoryCustom {
    override fun findFilter(afterSaleQuery: AfterSaleQuery): MutableList<AfterSaleDto> {
        val sb = StringBuilder()
        sb.append("""
             SELECT
                 t1.*,t4.name as 'areaDepotName',t2.ime as 'badProductIme',t3.name as 'badProductName',
                 t6.name as 'toAreaProductName',t7.name as 'retailDepotName',t5.ime as 'toAreaProductIme',t8.name as 'fromCompanyProductName'
             FROM
                crm_after_sale  t1 left join crm_product_ime t2 on t1.bad_product_ime_id=t2.id
                left join crm_product t3 on t2.product_id=t3.id
                left join crm_depot t4 on t1.area_depot_id=t4.id
                left join crm_product_ime t5 on t1.to_area_product_ime_id=t5.id
                left join crm_product t6 on t5.product_id=t6.id
                left join crm_depot t7 on t2.retail_shop_id=t7.id
                left join crm_product t8 on t1.from_company_product_id=t8.id
             WHERE
                 t1.enabled=1
            """)
        if (afterSaleQuery.toStoreDateStart != null) {
            sb.append("""  and t1.to_store_date  >= :toStoreDateStart """)
        }
        if (afterSaleQuery.toStoreDateEnd!= null) {
            sb.append("""  and t1.to_store_date  < :toStoreDateEnd """)
        }
        if (afterSaleQuery.toCompanyDateStart != null) {
            sb.append("""  and t1.to_company_date  >= :toCompanyDateStart """)
        }
        if (afterSaleQuery.toCompanyDateEnd!= null) {
            sb.append("""  and t1.to_company_date  < :toCompanyDateEnd """)
        }
        if (afterSaleQuery.createdDateStart!= null) {
            sb.append("""  and t1.created_date  < :createdDateStart """)
        }
        if (afterSaleQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  >= :createdDateEnd """)
        }
        if (afterSaleQuery.fromCompanyDateStart != null) {
            sb.append("""  and t1.from_company_date  >= :fromCompanyDateStart """)
        }
        if (afterSaleQuery.fromCompanyDateEnd!= null) {
            sb.append("""  and t1.from_company_date  < :fromCompanyDateEnd """)
        }
        if (StringUtils.isNotBlank(afterSaleQuery.badProductName)) {
            sb.append("""  and t3.name like concat('%',:badProductName,'%') """)
        }
        if (StringUtils.isNotBlank(afterSaleQuery.depotName)) {
            sb.append("""  and t4.name like concat('%',:depotName,'%') """)
        }
        if (CollectionUtil.isNotEmpty(afterSaleQuery.imeList)) {
            sb.append("""  and t2.ime in (:imeList) """)
        }
        if (CollectionUtil.isNotEmpty(afterSaleQuery.toAreaImeList)) {
            sb.append("""  and t5.ime in (:toAreaImeList) """)
        }
        if (CollectionUtil.isNotEmpty(afterSaleQuery.businessIdList)) {
            sb.append("""  and t1.business_id in (:businessIdList) """)
        }
        if (afterSaleQuery.fromCompany) {
            sb.append("""  and t1.from_company_date is  null """)
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(afterSaleQuery), BeanPropertyRowMapper(AfterSaleDto::class.java));
    }

    override fun findMaxBusinessId(dateStart: LocalDate): MutableList<String> {
        val sb = StringBuilder()
        sb.append("""
            SELECT MAX(t1.business_id)
            FROM crm_after_sale  t1
            WHERE t1.created_date >= :dateStart
        """)
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", dateStart);
        return namedParameterJdbcTemplate.queryForList(sb.toString(), paramMap, String::class.java)
    }

    override fun findDtoByIds(ids: MutableList<String>): MutableList<AfterSaleDto> {
        val sb = StringBuilder()
        sb.append("""
             SELECT
                 t1.*,t2.ime as 'badProductIme',t3.name as 'badProductName',t4.name as 'badDepotName'
             FROM
                 crm_after_sale  t1 left join crm_product_ime t2 on t1.bad_product_ime_id=t2.id
                 left join crm_product t3 on t1.bad_product_id=t3.id
                 left join crm_depot t4 on t1.bad_depot_id=t4.id
                 left join crm_after_sale_flee t5 on t5.after_sale_id=t1.id
             WHERE
                 t1.enabled=1
                and t1.id in(:ids)
        """)
        var paramMap = HashMap<String, Any>()
        paramMap.put("ids", ids);
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(AfterSaleDto::class.java))
    }

    override fun findPage(pageable: Pageable, afterSaleQuery: AfterSaleQuery): Page<AfterSaleDto> {
            val sb = StringBuilder()
            sb.append("""
             SELECT
                 t1.*,t4.name as 'areaDepotName',t2.ime as 'badProductIme',t3.name as 'badProductName',
                 t6.name as 'toAreaProductName',t7.name as 'retailDepotName',t5.ime as 'toAreaProductIme',t8.name as 'fromCompanyProductName'
             FROM
                crm_after_sale  t1 left join crm_product_ime t2 on t1.bad_product_ime_id=t2.id
                left join crm_product t3 on t2.product_id=t3.id
                left join crm_depot t4 on t1.area_depot_id=t4.id
                left join crm_product_ime t5 on t1.to_area_product_ime_id=t5.id
                left join crm_product t6 on t5.product_id=t6.id
                left join crm_depot t7 on t2.retail_shop_id=t7.id
                left join crm_product t8 on t1.from_company_product_id=t8.id
             WHERE
                 t1.enabled=1
            """)
            if (afterSaleQuery.toStoreDateStart != null) {
                sb.append("""  and t1.to_store_date  >= :toStoreDateStart """)
            }
            if (afterSaleQuery.toStoreDateEnd!= null) {
                sb.append("""  and t1.to_store_date  < :toStoreDateEnd """)
            }
            if (afterSaleQuery.toCompanyDateStart != null) {
                sb.append("""  and t1.to_company_date  >= :toCompanyDateStart """)
            }
            if (afterSaleQuery.toCompanyDateEnd!= null) {
                sb.append("""  and t1.to_company_date  < :toCompanyDateEnd """)
            }
            if (afterSaleQuery.createdDateStart!= null) {
                sb.append("""  and t1.created_date  < :createdDateStart """)
            }
            if (afterSaleQuery.createdDateEnd != null) {
                sb.append("""  and t1.created_date  >= :createdDateEnd """)
            }
            if (afterSaleQuery.fromCompanyDateStart != null) {
                sb.append("""  and t1.from_company_date  >= :fromCompanyDateStart """)
            }
            if (afterSaleQuery.fromCompanyDateEnd!= null) {
                sb.append("""  and t1.from_company_date  < :fromCompanyDateEnd """)
            }
            if (StringUtils.isNotBlank(afterSaleQuery.badProductName)) {
                sb.append("""  and t3.name like concat('%',:badProductName,'%') """)
            }
            if (StringUtils.isNotBlank(afterSaleQuery.depotName)) {
                sb.append("""  and t4.name like concat('%',:depotName,'%') """)
            }
            if (CollectionUtil.isNotEmpty(afterSaleQuery.imeList)) {
                sb.append("""  and t2.ime in (:imeList) """)
            }
            if (CollectionUtil.isNotEmpty(afterSaleQuery.toAreaImeList)) {
                sb.append("""  and t5.ime in (:toAreaImeList) """)
            }
            if (CollectionUtil.isNotEmpty(afterSaleQuery.businessIdList)) {
                sb.append("""  and t1.business_id in (:businessIdList) """)
            }
            var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
            var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
            var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(afterSaleQuery), BeanPropertyRowMapper(AfterSaleDto::class.java));
            var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(afterSaleQuery),Long::class.java);
            return PageImpl(list,pageable,count);
    }

    override fun findDtoByImeIn(imeList: MutableList<String>): MutableList<AfterSaleDto> {
        val sb = StringBuilder()
        sb.append("""
             SELECT
                 t1.*,t4.name as 'areaDepotName',t2.ime as 'badProductIme',t3.name as 'badProductName',
                 t6.name as 'toAreaProductName',t7.name as 'retailDepotName',t5.ime as 'toAreaProductIme',t8.name as 'fromCompanyProductName'
             FROM
                crm_after_sale  t1 left join crm_product_ime t2 on t1.bad_product_ime_id=t2.id
                left join crm_product t3 on t2.product_id=t3.id
                left join crm_depot t4 on t1.area_depot_id=t4.id
                left join crm_product_ime t5 on t1.to_area_product_ime_id=t5.id
                left join crm_product t6 on t5.product_id=t6.id
                left join crm_depot t7 on t2.retail_shop_id=t7.id
                left join crm_product t8 on t1.from_company_product_id=t8.id
             WHERE
                 t1.enabled=1
                AND t2.ime in (:imeList)
            """)
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("imeList" ,imeList);
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(AfterSaleDto::class.java));
    }
}