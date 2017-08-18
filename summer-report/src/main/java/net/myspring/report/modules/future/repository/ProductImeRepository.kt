package net.myspring.report.modules.future.repository

import net.myspring.report.modules.future.dto.ProductImeReportDto
import net.myspring.report.modules.future.web.query.ReportQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.lang.StringBuilder

@Component
class ProductImeRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

     fun findBaokaStoreReport(productImeReportQuery: ReportQuery): MutableList<ProductImeReportDto> {
        val sb = StringBuilder()
        if (StringUtils.isBlank(productImeReportQuery.officeId) && productImeReportQuery.sumType == "区域") {
            sb.append(""" select t3.area_id as 'officeId',count(t1.id) as 'qty' """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.officeId) && productImeReportQuery.sumType == "区域") {
            sb.append(""" select t3.office_id,t1.id,t1.ime """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType) && productImeReportQuery.sumType == "型号") {
            sb.append(""" select t5.id as 'productTypeId',count(t1.id) as 'qty',t5.name as 'productTypeName' """)
        }
        sb.append("""
            FROM
                crm_product_ime t1
                LEFT JOIN crm_depot t3 on t1.depot_id=t3.id and t3.depot_store_id is null
                LEFT JOIN crm_product t4 on t1.product_id=t4.id
                LEFT JOIN crm_product_type t5 on t4.product_type_id=t5.id,
                crm_depot_shop t6
            WHERE
                 t1.enabled = 1
                and t3.depot_shop_id=t6.id
    """)
        if (productImeReportQuery.date != null) {
            sb.append("""
                AND (
                    t1.retail_date IS NULL
                    OR t1.retail_date >= :date
                )
                AND t1.created_date < :date
            """)
        }
        if (productImeReportQuery.scoreType != null) {
            sb.append(""" and t5.score_type=:scoreType """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.townType)) {
            sb.append(""" and t6.town_type=:townType """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.areaType)) {
            sb.append(""" and t6.area_type=:areaType """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.productTypeIdList)) {
            sb.append(""" and t5.id in (:productTypeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.officeIdList)) {
            sb.append(""" and t3.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.officeIds)) {
            sb.append(""" and t3.office_id in (:officeIds) """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.depotIdList)) {
            sb.append(""" and t3.id in (:depotIdList) """)
        }
        if (StringUtils.isBlank(productImeReportQuery.officeId) && productImeReportQuery.sumType == "区域") {
            sb.append(""" group by t3.area_id """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType) && productImeReportQuery.sumType == "型号") {
            sb.append(""" group by t5.id """)
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productImeReportQuery), BeanPropertyRowMapper(ProductImeReportDto::class.java))
    }

     fun findStoreReport(productImeReportQuery: ReportQuery): MutableList<ProductImeReportDto> {
        val sb = StringBuilder()
        if (StringUtils.isBlank(productImeReportQuery.officeId) && productImeReportQuery.sumType == "区域") {
            sb.append(""" select t6.area_id as 'officeId',count(t1.id) as 'qty' """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.officeId) && productImeReportQuery.sumType == "区域") {
            sb.append(""" select t6.office_id,t1.id ,t1.ime""")
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType) && productImeReportQuery.sumType == "型号") {
            sb.append(""" select t5.id as 'productTypeId',count(t1.id) as 'qty' ,t5.name as 'productTypeName'""")
        }
        sb.append("""
                FROM
                    crm_product_ime t1
                    LEFT JOIN crm_product_ime_sale t3 ON t1.product_ime_sale_id = t3.id
                    LEFT JOIN crm_product t4 on t1.product_id=t4.id
                    LEFT JOIN crm_product_type t5 on t4.product_type_id=t5.id
                    LEFT JOIN crm_depot t6 on t1.depot_id=t6.id and t6.depot_store_id is null,
                    crm_depot_shop t7
                    WHERE
                    t1.enabled = 1
                    and t6.depot_shop_id=t7.id
    """)
        if (productImeReportQuery.scoreType != null) {
            sb.append("""  and t5.score_type =:scoreType """)
        }
        if (productImeReportQuery.date != null) {
            sb.append("""
                AND (
                    t3.id IS NULL
                    OR t3.created_date > :date
                )
                AND t1.created_date < :date
            """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.townType)) {
            sb.append(""" and t7.town_type=:townType """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.areaType)) {
            sb.append(""" and t7.area_type=:areaType """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.productTypeIdList)) {
            sb.append(""" and t5.id in (:productTypeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.officeIdList)) {
            sb.append(""" and t6.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.officeIds)) {
            sb.append(""" and t6.office_id in (:officeIds) """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.depotIdList)) {
            sb.append(""" and t6.id in (:depotIdList) """)
        }
        if (StringUtils.isBlank(productImeReportQuery.officeId) && productImeReportQuery.sumType == "区域") {
            sb.append(""" group by t6.area_id """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType) && productImeReportQuery.sumType == "型号") {
            sb.append(""" group by t5.id """)
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productImeReportQuery), BeanPropertyRowMapper(ProductImeReportDto::class.java))
    }

     fun findSaleReport(productImeSaleReportQuery: ReportQuery): MutableList<ProductImeReportDto> {
        val sb = StringBuilder()
        if (StringUtils.isBlank(productImeSaleReportQuery.officeId) && productImeSaleReportQuery.sumType == "区域") {
            sb.append(""" select t2.area_id as 'officeId',count(t1.id) as 'qty' """)
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.officeId) && productImeSaleReportQuery.sumType == "区域") {
            sb.append(""" select t2.office_id,t1.id ,t1.ime""")
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.sumType) && productImeSaleReportQuery.sumType == "型号") {
            sb.append(""" select t5.id as 'productTypeId',count(t1.id) as 'qty',t5.name as 'productTypeName' """)
        }
        sb.append("""
            FROM
            crm_product_ime_sale t1
            LEFT JOIN crm_depot t2 ON t1.shop_id = t2.id and t2.depot_store_id is null
            LEFT JOIN crm_product_ime t3 on t1.product_ime_id = t3.id
            LEFT JOIN crm_product t4 on t3.product_id=t4.id
            LEFT JOIN crm_product_type t5 on t4.product_type_id=t5.id,
            crm_depot_shop t6
            WHERE
             t1.enabled = 1
            and t1.is_back=0
            and t2.depot_shop_id=t6.id
    """)
        if (productImeSaleReportQuery.dateStart != null) {
            sb.append(""" and t1.created_date>=:dateStart """)
        }
        if (productImeSaleReportQuery.dateEnd != null) {
            sb.append(""" and t1.created_date<:dateEnd """)
        }
        if (productImeSaleReportQuery.scoreType != null) {
            sb.append(""" and t5.score_type=:scoreType""")
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.townType)) {
            sb.append(""" and t6.town_type=:townType """)
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.areaType)) {
            sb.append(""" and t6.area_type=:areaType """)
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.netType)) {
            sb.append(""" and t4.net_type = :netType """)
        }
        if (CollectionUtil.isNotEmpty(productImeSaleReportQuery.productTypeIdList)) {
            sb.append(""" and t5.id in (:productTypeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeSaleReportQuery.officeIdList)) {
            sb.append(""" and t2.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeSaleReportQuery.officeIds)) {
            sb.append(""" and t2.office_id in (:officeIds) """)
        }
        if (CollectionUtil.isNotEmpty(productImeSaleReportQuery.depotIdList)) {
            sb.append(""" and t2.id in (:depotIdList) """)
        }
        if (StringUtils.isBlank(productImeSaleReportQuery.officeId) && productImeSaleReportQuery.sumType == "区域") {
            sb.append(""" group by t2.area_id """)
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.sumType) && productImeSaleReportQuery.sumType == "型号") {
            sb.append(""" group by t5.id """)
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productImeSaleReportQuery), BeanPropertyRowMapper(ProductImeReportDto::class.java))
    }

     fun findBaokaSaleReport(productImeSaleReportQuery: ReportQuery): MutableList<ProductImeReportDto> {
        val sb = StringBuilder()
        if (StringUtils.isBlank(productImeSaleReportQuery.officeId) && productImeSaleReportQuery.sumType == "区域") {
            sb.append(""" select t2.area_id as 'officeId',count(t1.id) as 'qty' """)
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.officeId) && productImeSaleReportQuery.sumType == "区域") {
            sb.append(""" select t2.office_id,t1.id,t1.ime """)
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.sumType) && productImeSaleReportQuery.sumType == "型号") {
            sb.append(""" select t4.id  as 'productTypeId',count(t1.id) as 'qty',t4.name as 'productTypeName' """)
        }
        sb.append("""
            FROM
                crm_product_ime t1
            LEFT JOIN crm_depot t2 ON t1.depot_id = t2.id and t2.depot_store_id is null
            LEFT JOIN crm_product t3 ON t1.product_id = t3.id
            LEFT JOIN crm_product_type t4 ON t3.product_type_id = t4.id,
            crm_depot_shop t5
            where t1.enabled=1
            and t2.depot_shop_id=t5.id
    """)
        if (productImeSaleReportQuery.dateStart != null) {
            sb.append(""" and t1.retail_date>=:dateStart """)
        }
        if (productImeSaleReportQuery.dateEnd != null) {
            sb.append(""" and t1.retail_date<:dateEnd """)
        }
        if (productImeSaleReportQuery.scoreType != null) {
            sb.append(""" and t4.score_type=:scoreType """)
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.townType)) {
            sb.append(""" and t5.town_type=:townType """)
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.areaType)) {
            sb.append(""" and t5.area_type=:areaType """)
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.netType)) {
            sb.append(""" and t3.net_type = :netType """)
        }
        if (CollectionUtil.isNotEmpty(productImeSaleReportQuery.productTypeIdList)) {
            sb.append(""" and t4.id in (:productTypeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeSaleReportQuery.officeIdList)) {
            sb.append(""" and t2.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeSaleReportQuery.officeIds)) {
            sb.append(""" and t2.office_id in (:officeIds) """)
        }
        if (CollectionUtil.isNotEmpty(productImeSaleReportQuery.depotIdList)) {
            sb.append(""" and t2.id in (:depotIdList) """)
        }
        if (StringUtils.isBlank(productImeSaleReportQuery.officeId) && productImeSaleReportQuery.sumType == "区域") {
            sb.append(""" group by t2.area_id """)
        }
        if (StringUtils.isNotBlank(productImeSaleReportQuery.sumType) && productImeSaleReportQuery.sumType == "型号") {
            sb.append(""" group by t4.id """)
        }
        println("销售报表按电子报卡" + productImeSaleReportQuery.officeIdList)
        println(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productImeSaleReportQuery), BeanPropertyRowMapper(ProductImeReportDto::class.java))
    }
}