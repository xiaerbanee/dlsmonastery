package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DepotShop
import net.myspring.future.modules.basic.dto.DepotReportDto
import net.myspring.future.modules.basic.dto.DepotShopDto
import net.myspring.future.modules.basic.web.query.DepotQuery
import net.myspring.future.modules.basic.web.query.DepotShopQuery
import net.myspring.future.modules.crm.web.query.ReportQuery
import net.myspring.future.modules.layout.dto.ShopAllotDto
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
import java.util.*

/**
 * Created by zhangyf on 2017/5/24.
 */
interface DepotShopRepository : BaseRepository<DepotShop,String>,DepotShopRepositoryCustom {

}


interface DepotShopRepositoryCustom{

    fun findPage(pageable: Pageable, depotShopQuery: DepotShopQuery): Page<DepotShopDto>?

    fun findFilter(depotShopQuery: DepotShopQuery): MutableList<DepotShopDto>

    fun findSaleReport(reportQuery: ReportQuery):MutableList<DepotReportDto>

    fun findBaokaSaleReport(reportQuery: ReportQuery):MutableList<DepotReportDto>

    fun findStoreReport(reportQuery: ReportQuery):MutableList<DepotReportDto>

    fun findBaokaStoreReport(reportQuery: ReportQuery):MutableList<DepotReportDto>
    
    fun findDto(id:String):DepotShopDto;





}

class DepotShopRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):DepotShopRepositoryCustom{
    override fun findFilter(depotShopQuery: DepotShopQuery): MutableList<DepotShopDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.id AS 'depotId',
                t1.name as 'depotName',
                t1.area_id areaId,
                t1.office_id officeId,
                t1.contator,
                t1.mobile_phone mobilePhone,
                t2.*,
                t3.name as 'pricesystemName',
                t4.name as 'clientName',
                t5.name as 'chainName'
            FROM
                crm_depot t1 left join crm_pricesystem t3 on t1.pricesystem_id=t3.id
                left join crm_client t4 on t1.client_id=t4.id
                left join crm_chain t5 on t1.chain_id =t5.id,
                crm_depot_shop t2
            WHERE
                t1.enabled = 1
            AND t2.enabled = 1
            AND t1.depot_shop_id = t2.id
        """)
        if (StringUtils.isNotEmpty(depotShopQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.areaType)) {
            sb.append("""  and t1.area_type =:areaType  """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.pricesystemId)) {
            sb.append("""  and t1.pricesystem_id =:pricesystemId """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.contator)) {
            sb.append("""  and t1.contator LIKE CONCAT('%',:contator,'%') """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.mobilePhone)) {
            sb.append("""  and t1.mobile_phone LIKE CONCAT('%',:mobilePhone,'%') """)
        }
        if (depotShopQuery.specialityStore !=null) {
            sb.append("""  and t1.speciality_store =:specialityStore """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.specialityStoreType)) {
            sb.append("""  and t1.speciality_store_type =:specialityStoreType """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.officeId)) {
            sb.append("""  and t1.office_id =:officeId """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.chainId)) {
            sb.append("""  and t1.chain_id =:chainId """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.adPricesystemId)) {
            sb.append("""  and t1.ad_pricesystem_id =:adPricesystemId """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.expressCompanyId)) {
            sb.append("""  and t1.express_company_id =:expressCompanyId """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.districtId)) {
            sb.append("""  and t1.district_id =:districtId """)
        }
        if (depotShopQuery.adShopBsc !=null) {
            sb.append("""  and t1.ad_shop_bsc =:adShopBsc  """)
        }
        if (depotShopQuery.adShop !=null) {
            sb.append("""  and t1.ad_shop =:adShop""")
        }
        if (depotShopQuery.hidden !=null) {
            sb.append("""  and t1.is_hidden =:hidden """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(depotShopQuery), BeanPropertyRowMapper(DepotShopDto::class.java))
    }

    override fun findDto(id: String): DepotShopDto {
        return namedParameterJdbcTemplate.queryForObject("""
              SELECT
                t1.id AS 'depotId',
                t1.name as 'depotName',
                t1.contator,
                t1.mobile_phone,
                t1.address,
                t1.office_id,
                t2.*
            FROM
                crm_depot t1,
                crm_depot_shop t2
            WHERE
                t1.enabled = 1
            AND t2.enabled = 1
            AND t1.depot_shop_id = t2.id
            AND t2.id=:id
          """, Collections.singletonMap("id", id), BeanPropertyRowMapper(DepotShopDto::class.java))
    }

    override fun findBaokaStoreReport(reportQuery: ReportQuery): MutableList<DepotReportDto> {
        val sb = StringBuffer()
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append("""   SELECT t6.id as depotId,t6.name as 'depotName', COUNT(t1.id) AS qty,t8.name as 'chainName',t5.name as 'productTypeName',t6.office_id as 'officeId',t6.area_id as 'areaId'""")
        }else if(reportQuery.isDetail){
            sb.append("""
               SELECT t4.id as 'productId',t4.name as 'productName',t1.ime,t6.name as 'depotName',
               t8.name as 'chainName',t5.name as 'productTypeName',t6.office_id as 'officeId',t6.area_id as 'areaId'
            """)
        }
        sb.append("""
                    FROM
                    crm_product_ime t1
                    LEFT JOIN crm_product_ime_upload t2 ON t1.product_ime_upload_id = t2.id
                    LEFT JOIN crm_product t4 on t1.product_id=t4.id
                    LEFT JOIN crm_product_type t5 on t4.product_type_id=t5.id
                    LEFT JOIN crm_depot t6 on t1.depot_id=t6.id
                    LEFT JOIN crm_chain t8 on t6.chain_id=t8.id,
                    crm_depot_shop t7
                    WHERE
                    t1.enabled = 1
                    and  t6.depot_shop_id=t7.id
        """)
        if(reportQuery.scoreType!=null){
            sb.append("""  and t5.score_type =:scoreType """)
        }
        if(reportQuery.date!=null){
            sb.append("""
                AND (
                    t2.id IS NULL
                    OR t2.created_date > :date
                )
                AND (
                    t1.retail_date IS NULL
                    OR t1.retail_date > :date
                )
                AND t1.created_date < :date
            """)
        }
        if (StringUtils.isNotBlank(reportQuery.townType)) {
            sb.append(""" and t7.town_type=:townType """)
        }
        if (StringUtils.isNotBlank(reportQuery.areaType)) {
            sb.append(""" and t7.area_type=:areaType """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.productTypeIdList)) {
            sb.append(""" and t5.id in (:productTypeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.officeIdList)) {
            sb.append(""" and t6.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.officeIds)) {
            sb.append(""" and t6.office_id in (:officeIds) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.depotIdList)) {
            sb.append(""" and t6.id in (:depotIdList) """)
        }
        if (StringUtils.isNotBlank(reportQuery.officeId)) {
            sb.append(""" and t6.office_id =:officeId""")
        }
        if (StringUtils.isNotBlank(reportQuery.depotId)) {
            sb.append(""" and t6.id=:depotId """)
        }
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append(""" group by t1.depot_id""")
        }
        if(StringUtils.isNotBlank(reportQuery.exportType)&&reportQuery.exportType=="按数量"){
            sb.append(""" ,t5.id""")
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(reportQuery), BeanPropertyRowMapper(DepotReportDto::class.java))
    }

    override fun findStoreReport(reportQuery: ReportQuery): MutableList<DepotReportDto> {
        val sb = StringBuffer()
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append("""  SELECT t6.id as depotId,t6.name as depotName,COUNT(t1.id) AS qty ,t8.name as 'chainName',t5.name as 'productTypeName',t6.office_id as 'officeId',t6.area_id as 'areaId'""")
        }else if(reportQuery.isDetail){
            sb.append("""
               SELECT t4.id as 'productId',t4.name as 'productName',t1.ime,t6.name as 'depotName',t8.name as 'chainName',t5.name as 'productTypeName',t6.office_id as 'officeId',t6.area_id as 'areaId'
            """)
        }
        sb.append("""
                    FROM
                    crm_product_ime t1
                    LEFT JOIN crm_product_ime_upload t2 ON t1.product_ime_upload_id = t2.id
                    LEFT JOIN crm_product_ime_sale t3 ON t1.product_ime_sale_id = t3.id
                    LEFT JOIN crm_product t4 on t1.product_id=t4.id
                    LEFT JOIN crm_product_type t5 on t4.product_type_id=t5.id
                    LEFT JOIN crm_depot t6 on t1.depot_id=t6.id
                    LEFT JOIN crm_chain t8 on t6.chain_id=t8.id,
                    crm_depot_shop t7
                    WHERE
                    t1.enabled = 1
                    and t6.depot_shop_id=t7.id
        """)
        if(reportQuery.scoreType!=null){
            sb.append("""  and t5.score_type =:scoreType """)
        }
        if(reportQuery.date!=null){
            sb.append("""
                AND (
                    t1.retail_date IS NULL
                    OR t1.retail_date > :date
                )
                AND (
                    t2.id IS NULL
                    OR t2.created_date > :date
                )
                AND (
                    t3.id IS NULL
                    OR t3.created_date > :date
                )
                AND t1.created_date <= :date
            """)
        }
        if (StringUtils.isNotBlank(reportQuery.townType)) {
            sb.append(""" and t7.town_type=:townType """)
        }
        if (StringUtils.isNotBlank(reportQuery.areaType)) {
            sb.append(""" and t7.area_type=:areaType """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.productTypeIdList)) {
            sb.append(""" and t5.id in (:productTypeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.officeIdList)) {
            sb.append(""" and t6.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.officeIds)) {
            sb.append(""" and t6.office_id in (:officeIds) """)
        }
        if (StringUtils.isNotBlank(reportQuery.officeId)) {
            sb.append(""" and t6.office_id =:officeId""")
        }
        if (CollectionUtil.isNotEmpty(reportQuery.depotIdList)) {
            sb.append(""" and t6.id in (:depotIdList) """)
        }
        if (StringUtils.isNotBlank(reportQuery.depotId)) {
            sb.append(""" and t6.id=:depotId """)
        }
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append(""" group by t1.depot_id""")
        }
        if(StringUtils.isNotBlank(reportQuery.exportType)&&reportQuery.exportType=="按数量"){
            sb.append(""" ,t5.id""")
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(reportQuery), BeanPropertyRowMapper(DepotReportDto::class.java))
    }


    override fun findBaokaSaleReport(reportQuery: ReportQuery): MutableList<DepotReportDto> {
        val sb = StringBuffer()
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append("""  SELECT t4.id as 'depotId',t4.name as 'depotName', COUNT(t1.id) AS qty,t7.name as 'chainName',t3.name as 'productTypeName',t4.office_id as 'officeId',t4.area_id as 'areaId' """)
        }else if(reportQuery.isDetail){
            sb.append("""
               SELECT t2.id as 'productId',t2.name as 'productName',t1.ime,t1.retail_date,t6.employee_id,t4.office_id as 'officeId',t4.area_id as 'areaId',
               t6.created_date as 'saleDate',t4.id as 'depotId',t4.name as 'depotName',t3.name as 'productTypeName',t7.name as 'chainName'
            """)
        }
        sb.append("""
                    FROM
                    crm_product_ime t1
                    LEFT JOIN crm_product t2 ON t1.product_id = t2.id
                    LEFT JOIN crm_product_type t3 on t2.product_type_id=t3.id
                    LEFT JOIN crm_depot t4 on t1.depot_id=t4.id
                    LEFT JOIN crm_chain t7 on t4.chain_id=t7.id
        """)
        if(reportQuery.isDetail!=null&&reportQuery.isDetail){
            sb.append(""" LEFT JOIN crm_product_ime_sale t6 on t1.product_ime_sale_id=t6.id """)
        }
        sb.append(""" ,crm_depot_shop t5   WHERE t1.enabled = 1 and t4.depot_shop_id=t5.id """)
        if(reportQuery.scoreType!=null){
            sb.append("""  and t3.score_type =:scoreType """)
        }
        if(reportQuery.dateStart!=null){
            sb.append(""" and t1.retail_date>=:dateStart """)
        }
        if(reportQuery.dateEnd!=null){
            sb.append(""" and t1.retail_date<=:dateEnd """)
        }
        if (StringUtils.isNotBlank(reportQuery.townType)) {
            sb.append(""" and t5.town_type=:townType """)
        }
        if (StringUtils.isNotBlank(reportQuery.areaType)) {
            sb.append(""" and t5.area_type=:areaType """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.productTypeIdList)) {
            sb.append(""" and t3.id in (:productTypeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.officeIdList)) {
            sb.append(""" and t4.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.officeIds)) {
            sb.append(""" and t4.office_id in (:officeIds) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.depotIdList)) {
            sb.append(""" and t4.id in (:depotIdList) """)
        }
        if (StringUtils.isNotBlank(reportQuery.officeId)) {
            sb.append(""" and t4.office_id =:officeId""")
        }
        if (StringUtils.isNotBlank(reportQuery.depotId)) {
            sb.append(""" and t4.id=:depotId """)
        }
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append(""" group by t1.depot_id""")
        }
        if(StringUtils.isNotBlank(reportQuery.exportType)&&reportQuery.exportType=="按数量"){
            sb.append(""" ,t3.id""")
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(reportQuery), BeanPropertyRowMapper(DepotReportDto::class.java))
    }

    override fun findSaleReport(reportQuery: ReportQuery): MutableList<DepotReportDto> {
        val sb = StringBuffer()
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append("""  SELECT t5.id as 'depotId',t5.name as 'depotName', COUNT(t1.id) AS qty,t7.name as 'chainName',t4.name as 'productTypeName',t5.office_id as 'officeId',t5.area_id as 'areaId'""")
        }else if(reportQuery.isDetail){
            sb.append("""
               SELECT t3.id as 'productId',t3.name as 'productName',t2.ime,t2.retail_date,t1.employee_id,t5.office_id as 'officeId',t5.area_id as 'areaId',
               t1.created_date as 'saleDate',t5.id as 'depotId',t5.name as 'depotName',t7.name as 'chainName',t4.name as 'productTypeName'
            """)
        }
        sb.append("""
                    FROM
                    crm_product_ime_sale t1
                    LEFT JOIN crm_product_ime t2 ON t2.product_ime_sale_id = t1.id
                    LEFT JOIN crm_product t3 on t2.product_id=t3.id
                    LEFT JOIN crm_product_type t4 on t3.product_type_id=t4.id
                    LEFT JOIN crm_depot t5 on t1.shop_id=t5.id
                    LEFT JOIN crm_chain t7 on t5.chain_id=t7.id,
                    crm_depot_shop t6
                    WHERE
                    t1.enabled = 1
                    and t5.depot_shop_id=t6.id
        """)
        if(reportQuery.scoreType!=null){
            sb.append("""  and t4.score_type =:scoreType """)
        }
        if(reportQuery.dateStart!=null){
            sb.append(""" and t1.created_date>=:dateStart """)
        }
        if(reportQuery.dateEnd!=null){
            sb.append(""" and t1.created_date<=:dateEnd """)
        }
        if (StringUtils.isNotBlank(reportQuery.townType)) {
            sb.append(""" and t6.town_type=:townType """)
        }
        if (StringUtils.isNotBlank(reportQuery.areaType)) {
            sb.append(""" and t6.area_type=:areaType """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.productTypeIdList)) {
            sb.append(""" and t4.id in (:productTypeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.officeIdList)) {
            sb.append(""" and t5.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.officeIds)) {
            sb.append(""" and t5.office_id in (:officeIds) """)
        }
        if (CollectionUtil.isNotEmpty(reportQuery.depotIdList)) {
            sb.append(""" and t5.id in (:depotIdList) """)
        }
        if (StringUtils.isNotBlank(reportQuery.officeId)) {
            sb.append(""" and t5.office_id =:officeId""")
        }
        if (StringUtils.isNotBlank(reportQuery.depotId)) {
            sb.append(""" and t5.id=:depotId """)
        }
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append(""" group by t1.shop_id""")
        }
        if(StringUtils.isNotBlank(reportQuery.exportType)&&reportQuery.exportType=="按数量"){
            sb.append(""" ,t4.id""")
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(reportQuery), BeanPropertyRowMapper(DepotReportDto::class.java))
    }

    override fun findPage(pageable: Pageable, depotShopQuery: DepotShopQuery): Page<DepotShopDto>? {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.id AS 'depotId',
                t1.name as 'depotName',
                t1.area_id areaId,
                t1.office_id officeId,
                t1.contator,
                t1.mobile_phone mobilePhone,
                t2.*,
                t3.name as 'pricesystemName',
                t4.name as 'clientName',
                t5.name as 'chainName'
            FROM
                crm_depot t1 left join crm_pricesystem t3 on t1.pricesystem_id=t3.id
                left join crm_client t4 on t1.client_id=t4.id
                left join crm_chain t5 on t1.chain_id =t5.id,
                crm_depot_shop t2
            WHERE
                t1.enabled = 1
            AND t2.enabled = 1
            AND t1.depot_shop_id = t2.id
        """)
        if (StringUtils.isNotEmpty(depotShopQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.areaType)) {
            sb.append("""  and t1.area_type =:areaType  """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.pricesystemId)) {
            sb.append("""  and t1.pricesystem_id =:pricesystemId """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.contator)) {
            sb.append("""  and t1.contator LIKE CONCAT('%',:contator,'%') """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.mobilePhone)) {
            sb.append("""  and t1.mobile_phone LIKE CONCAT('%',:mobilePhone,'%') """)
        }
        if (depotShopQuery.specialityStore !=null) {
            sb.append("""  and t1.speciality_store =:specialityStore """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.specialityStoreType)) {
            sb.append("""  and t1.speciality_store_type =:specialityStoreType """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.officeId)) {
            sb.append("""  and t1.office_id =:officeId """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.chainId)) {
            sb.append("""  and t1.chain_id =:chainId """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.adPricesystemId)) {
            sb.append("""  and t1.ad_pricesystem_id =:adPricesystemId """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.expressCompanyId)) {
            sb.append("""  and t1.express_company_id =:expressCompanyId """)
        }
        if (StringUtils.isNotEmpty(depotShopQuery.districtId)) {
            sb.append("""  and t1.district_id =:districtId """)
        }
        if (depotShopQuery.adShopBsc !=null) {
            sb.append("""  and t1.ad_shop_bsc =:adShopBsc  """)
        }
        if (depotShopQuery.adShop !=null) {
            sb.append("""  and t1.ad_shop =:adShop""")
        }
        if (depotShopQuery.hidden !=null) {
            sb.append("""  and t1.is_hidden =:hidden """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(depotShopQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(DepotShopDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)
    }



}