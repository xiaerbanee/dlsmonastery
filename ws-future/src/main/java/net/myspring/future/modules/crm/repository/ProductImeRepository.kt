package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ProductIme
import net.myspring.future.modules.crm.dto.ProductImeDto
import net.myspring.future.modules.crm.dto.ProductImeForReportScoreDto
import net.myspring.future.modules.crm.dto.ProductImeHistoryDto
import net.myspring.future.modules.crm.dto.ProductImeReportDto
import net.myspring.future.modules.crm.web.query.ProductImeQuery
import net.myspring.future.modules.crm.web.query.ProductImeShipQuery
import net.myspring.future.modules.crm.web.query.ReportQuery
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
import java.lang.StringBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


interface ProductImeRepository : BaseRepository<ProductIme, String>, ProductImeRepositoryCustom{

    fun findByEnabledIsTrueAndCompanyIdAndIme(companyId: String, ime: String): ProductIme?

    fun findByEnabledIsTrueAndCompanyIdAndImeIn(companyId :String, imeList: MutableList<String>): MutableList<ProductIme>

    fun countByEnabledIsTrueAndDepotIdAndCompanyIdAndRetailDateBetween(depotId: String, companyId: String, retailDateStart: LocalDateTime, retailDateEnd: LocalDateTime): Long

    @Query("""
    SELECT
        t1
    FROM
        #{#entityName} t1
    WHERE
        t1.enabled = 1
        AND (
            t1.ime IN ?1
            OR t1.ime2 IN ?1
            OR t1.meid IN ?1
        )
        """)
    fun findByImeList(imeList: MutableList<String>): MutableList<ProductIme>


    fun findTop20ByDepotIdAndImeReverseStartingWithAndEnabledIsTrue(depotId: String,  ime: String): MutableList<ProductIme>

}


interface ProductImeRepositoryCustom{
    fun findPage(pageable: Pageable, productImeQuery : ProductImeQuery): Page<ProductImeDto>

    fun findProductImeHistoryList(productImeId: String): MutableList<ProductImeHistoryDto>

    fun findProductImeDto(productImeId: String): ProductImeDto

    fun findDtoListByImeList(imeList: MutableList<String>, companyId: String): MutableList<ProductImeDto>

    fun batchQuery(allImeList: List<String>, companyId: String): List<ProductImeDto>

    fun findShipList(productImeShipQuery: ProductImeShipQuery): MutableList<ProductIme>

    fun findForReportScore( dateStart :LocalDate,  dateEnd : LocalDate,   companyId :String) : MutableList<ProductImeForReportScoreDto>

    fun findBaokaSaleReport(productImeSaleReportQuery: ReportQuery) : MutableList<ProductImeReportDto>

    fun findSaleReport(productImeSaleReportQuery: ReportQuery) : MutableList<ProductImeReportDto>

    fun findBaokaStoreReport(productImeReportQuery: ReportQuery) : MutableList<ProductImeReportDto>

    fun findStoreReport(productImeReportQuery: ReportQuery) : MutableList<ProductImeReportDto>
}

class ProductImeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ProductImeRepositoryCustom {
    override fun batchQuery(allImeList: List<String>, companyId: String): List<ProductImeDto> {
        if(allImeList.isEmpty()){
            return ArrayList()
        }

        val params = HashMap<String, Any>()
        params.put("allImeList", allImeList)
        params.put("companyId", companyId)
        return namedParameterJdbcTemplate.query("""
        SELECT
            sale.created_date productImeSaleCreatedDate,
            sale.created_by productImeSaleCreatedBy,
            sale.employee_id productImeSaleEmployeeId,
            sale.shop_id productImeSaleShopId,
            upload.created_date productImeUploadCreatedDate,
            upload.created_by productImeUploadCreatedBy,
            upload.employee_id productImeUploadEmployeeId,
            upload.shop_id productImeUploadShopId,
            upload.status productImeUploadStatus,
            upload.month productImeUploadMonth,
            upload.id productImeUploadId,
            validProductIme.*
        FROM
        (
            SELECT
            product.product_type_id, depot.office_id depotOfficeId, t1.*
            FROM
            crm_product_ime t1,
            crm_depot depot,
            crm_product product
            WHERE
                t1.enabled = 1
                AND t1.depot_id = depot.id
                AND depot.enabled = 1
                AND t1.company_id =  :companyId
                AND t1.product_id =  product.id
                AND product.enabled =  1
                AND (t1.ime IN (:allImeList) OR t1.ime2 IN (:allImeList) OR t1.box_ime IN (:allImeList) OR t1.meid IN (:allImeList))
            ) validProductIme
            LEFT JOIN crm_product_ime_sale sale ON validProductIme.product_ime_sale_id = sale.id AND sale.enabled = 1
            LEFT JOIN crm_product_ime_upload upload ON validProductIme.product_ime_upload_id = upload.id AND upload.enabled = 1
                """, params, BeanPropertyRowMapper(ProductImeDto::class.java))
    }

    override fun findBaokaStoreReport(productImeReportQuery: ReportQuery): MutableList<ProductImeReportDto> {
        val sb = StringBuilder()
        if (StringUtils.isBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" select t3.area_id as 'officeId',count(t1.id) as 'qty' """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" select t3.office_id,t1.id """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType)&&productImeReportQuery.sumType=="型号") {
            sb.append(""" select t5.id as 'productTypeId',count(t1.id) as 'qty',t5.name as 'productTypeName' """)
        }
        sb.append("""
            FROM
                crm_product_ime t1
                LEFT JOIN crm_product_ime_upload t2 ON t1.product_ime_upload_id = t2.id
                LEFT JOIN crm_depot t3 on t1.depot_id=t3.id
                LEFT JOIN crm_product t4 on t1.product_id=t4.id
                LEFT JOIN crm_product_type t5 on t4.product_type_id=t5.id,
                crm_depot_shop t6
            WHERE
                 t1.enabled = 1
                and t3.depot_shop_id=t6.id
    """)
        if(productImeReportQuery.date!=null){
            sb.append("""
                AND (
                    t1.retail_date IS NULL
                    OR t1.retail_date >:date
                )
                AND (
                    t2.id IS NULL
                    OR t2.created_date > :date
                )
                AND t1.created_date < :date
            """)
        }
        if (productImeReportQuery.scoreType) {
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
        if (CollectionUtil.isNotEmpty(productImeReportQuery.depotIdList)) {
            sb.append(""" and t3.id in (:depotIdList) """)
        }
        if (StringUtils.isBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" group by t3.area_id """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType)&&productImeReportQuery.sumType=="型号") {
            sb.append(""" group by t5.id """)
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productImeReportQuery), BeanPropertyRowMapper(ProductImeReportDto::class.java))
    }

    override fun findStoreReport(productImeReportQuery: ReportQuery): MutableList<ProductImeReportDto> {
        val sb = StringBuilder()
        if (StringUtils.isBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" select t6.area_id as 'officeId',count(t1.id) as 'qty' """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" select t6.office_id,t1.id """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType)&&productImeReportQuery.sumType=="型号") {
            sb.append(""" select t5.id as 'productTypeId',count(t1.id) as 'qty' ,t5.name as 'productTypeName'""")
        }
        sb.append("""
                FROM
                    crm_product_ime t1
                    LEFT JOIN crm_product_ime_upload t2 ON t1.product_ime_upload_id = t2.id
                    LEFT JOIN crm_product_ime_sale t3 ON t1.product_ime_sale_id = t3.id
                    LEFT JOIN crm_product t4 on t1.product_id=t4.id
                    LEFT JOIN crm_product_type t5 on t4.product_type_id=t5.id
                    LEFT JOIN crm_depot t6 on t1.depot_id=t6.id,
                    crm_depot_shop t7
                    WHERE
                    t1.enabled = 1
                    and t6.depot_shop_id=t7.id
    """)
        if(productImeReportQuery.scoreType){
            sb.append("""  and t5.score_type =:scoreType """)
        }
        if(productImeReportQuery.date!=null){
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
        if (CollectionUtil.isNotEmpty(productImeReportQuery.depotIdList)) {
            sb.append(""" and t6.id in (:depotIdList) """)
        }
        if (StringUtils.isBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" group by t6.area_id """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType)&&productImeReportQuery.sumType=="型号") {
            sb.append(""" group by t5.id """)
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productImeReportQuery), BeanPropertyRowMapper(ProductImeReportDto::class.java))
    }

    override fun findSaleReport(productImeReportQuery: ReportQuery): MutableList<ProductImeReportDto> {
        val sb = StringBuilder()
        if (StringUtils.isBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" select t2.area_id as 'officeId',count(t1.id) as 'qty' """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" select t2.office_id,t1.id """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType)&&productImeReportQuery.sumType=="型号") {
            sb.append(""" select t5.id as 'productTypeId',count(t1.id) as 'qty',t5.name as 'productTypeName' """)
        }
        sb.append("""
            FROM
            crm_product_ime_sale t1
            LEFT JOIN crm_depot t2 ON t1.shop_id = t2.id
            LEFT JOIN crm_product_ime t3 on t1.product_ime_id = t3.id
            LEFT JOIN crm_product t4 on t3.product_id=t4.id
            LEFT JOIN crm_product_type t5 on t4.product_type_id=t5.id,
            crm_depot_shop t6
            WHERE
             t1.enabled = 1
            and t2.depot_shop_id=t6.id
    """)
        if(productImeReportQuery.dateStart!=null){
            sb.append(""" and t1.created_date>=:dateStart """)
        }
        if(productImeReportQuery.dateEnd!=null){
            sb.append(""" and t1.created_date<=:dateEnd """)
        }
        if (productImeReportQuery.scoreType) {
            sb.append(""" and t5.score_type=:scoreType""")
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
            sb.append(""" and t2.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.depotIdList)) {
            sb.append(""" and t2.id in (:depotIdList) """)
        }
        if (StringUtils.isBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" group by t2.area_id """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType)&&productImeReportQuery.sumType=="型号") {
            sb.append(""" group by t5.id """)
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productImeReportQuery), BeanPropertyRowMapper(ProductImeReportDto::class.java))
    }

    override fun findBaokaSaleReport(productImeReportQuery: ReportQuery): MutableList<ProductImeReportDto> {
        val sb = StringBuilder()
        if (StringUtils.isBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" select t2.area_id as 'officeId',count(t1.id) as 'qty' """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" select t2.office_id,t1.id """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType)&&productImeReportQuery.sumType=="型号") {
            sb.append(""" select t4.id  as 'productTypeId',count(t1.id) as 'qty',t4.name as 'productTypeName' """)
        }
        sb.append("""
            FROM
                crm_product_ime t1
            LEFT JOIN crm_depot t2 ON t1.depot_id = t2.id
            LEFT JOIN crm_product t3 ON t1.product_id = t3.id
            LEFT JOIN crm_product_type t4 ON t3.product_type_id = t4.id,
            crm_depot_shop t5
            where t1.enabled=1
            and t2.depot_shop_id=t5.id
    """)
        if(productImeReportQuery.dateStart!=null){
            sb.append(""" and t1.retail_date>=:dateStart """)
        }
        if(productImeReportQuery.dateEnd!=null){
            sb.append(""" and t1.retail_date<=:dateEnd """)
        }
        if (productImeReportQuery.scoreType) {
            sb.append(""" and t4.score_type=:scoreType """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.townType)) {
            sb.append(""" and t5.town_type=:townType """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.areaType)) {
            sb.append(""" and t5.area_type=:areaType """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.productTypeIdList)) {
            sb.append(""" and t4.id in (:productTypeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.officeIdList)) {
            sb.append(""" and t2.office_id in (:officeIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeReportQuery.depotIdList)) {
            sb.append(""" and t2.id in (:depotIdList) """)
        }
        if (StringUtils.isBlank(productImeReportQuery.officeId)&&productImeReportQuery.sumType=="区域") {
            sb.append(""" group by t2.area_id """)
        }
        if (StringUtils.isNotBlank(productImeReportQuery.sumType)&&productImeReportQuery.sumType=="型号") {
            sb.append(""" group by t4.id """)
        }
        println("销售报表按电子报卡"+productImeReportQuery.officeId)
        println(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productImeReportQuery), BeanPropertyRowMapper(ProductImeReportDto::class.java))
    }


    override fun findForReportScore(dateStart: LocalDate, dateEnd: LocalDate, companyId: String): MutableList<ProductImeForReportScoreDto> {

        val params = HashMap<String, Any>()
        params.put("dateStart", dateStart)
        params.put("dateEnd", dateEnd)
        params.put("companyId", companyId)

        return namedParameterJdbcTemplate.query("""

            SELECT
            product.product_type_id, depot.office_id depotOfficeId, type.price1 productTypePrice1, t1.*
            FROM
            crm_product_ime t1,
            crm_depot depot,
            crm_product product,
            crm_product_type type
            WHERE
                t1.enabled = 1
                AND t1.depot_id = depot.id
                AND depot.enabled = 1
                AND t1.company_id =  :companyId
                AND t1.product_id =  product.id
                AND product.product_type_id = type.id
                AND product.enabled = 1
                AND type.enabled = 1
                AND t1.retail_date IS NOT NULL
                AND t1.retail_date >= :dateStart
                AND t1.retail_date < :dateEnd

                """, params, BeanPropertyRowMapper(ProductImeForReportScoreDto::class.java))
    }

    override fun findShipList(productImeShipQuery: ProductImeShipQuery): MutableList<ProductIme> {
        if(CollectionUtil.isEmpty(productImeShipQuery.boxImeList) && CollectionUtil.isEmpty(productImeShipQuery.imeList)){
            return ArrayList()
        }

        val sb = StringBuilder()
        if(CollectionUtil.isNotEmpty(productImeShipQuery.boxImeList)){
            sb.append("""
            SELECT
                t1.*
            FROM
                crm_product_ime t1
            WHERE
                t1.enabled = 1
                AND t1.depot_id = :depotId
                AND t1.box_ime IN (:boxImeList)
        """)
        }
        if(productImeShipQuery.boxImeAndIme ){
            sb.append("    UNION   ")
        }
        if(CollectionUtil.isNotEmpty(productImeShipQuery.imeList)){
            sb.append("""
            SELECT
                t1.*
            FROM
                crm_product_ime t1
            WHERE
                t1.enabled = 1
                AND t1.depot_id = :depotId
                AND (
                    t1.ime IN (:imeList)
                    OR t1.ime2 IN (:imeList)
                    OR t1.meid IN (:imeList)
                )
        """)
        }

        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productImeShipQuery), BeanPropertyRowMapper(ProductIme::class.java))
    }

    override fun findDtoListByImeList(imeList: MutableList<String>, companyId: String): MutableList<ProductImeDto> {
        if(imeList.isEmpty()){
            return ArrayList()
        }

        val params = HashMap<String, Any>()
        params.put("imeList", imeList)
        params.put("companyId", companyId)
        return namedParameterJdbcTemplate.query("""
        SELECT
            sale.created_date productImeSaleCreatedDate,
            sale.created_by productImeSaleCreatedBy,
            sale.employee_id productImeSaleEmployeeId,
            sale.shop_id productImeSaleShopId,
            upload.created_date productImeUploadCreatedDate,
            upload.created_by productImeUploadCreatedBy,
            upload.employee_id productImeUploadEmployeeId,
            upload.shop_id productImeUploadShopId,
            upload.status productImeUploadStatus,
            upload.month productImeUploadMonth,
            upload.id productImeUploadId,
            validProductIme.*
        FROM
        (
            SELECT
            product.product_type_id, depot.office_id depotOfficeId, t1.*
            FROM
            crm_product_ime t1,
            crm_depot depot,
            crm_product product
            WHERE
                t1.enabled = 1
                AND t1.depot_id = depot.id
                AND depot.enabled = 1
                AND t1.company_id =  :companyId
                AND t1.product_id =  product.id
                AND product.enabled =  1
                AND t1.ime in (:imeList)
            ) validProductIme
            LEFT JOIN crm_product_ime_sale sale ON validProductIme.product_ime_sale_id = sale.id AND sale.enabled = 1
            LEFT JOIN crm_product_ime_upload upload ON validProductIme.product_ime_upload_id = upload.id AND upload.enabled = 1
                """, params, BeanPropertyRowMapper(ProductImeDto::class.java))
    }

    override fun findProductImeDto(productImeId: String): ProductImeDto {
        return namedParameterJdbcTemplate.queryForObject("""
        SELECT
            sale.created_date productImeSaleCreatedDate,
            upload.created_date productImeUploadCreatedDate,
            ime.*
        FROM
            crm_product_ime ime
            LEFT JOIN crm_product_ime_sale sale ON ime.product_ime_sale_id = sale.id AND sale.enabled = 1
            LEFT JOIN crm_product_ime_upload upload ON ime.product_ime_upload_id = upload.id AND upload.enabled = 1
        WHERE
            ime.enabled = 1
            AND ime.id = :productImeId
                """, Collections.singletonMap("productImeId", productImeId), BeanPropertyRowMapper(ProductImeDto::class.java))
    }

    override fun findProductImeHistoryList(productImeId: String): MutableList<ProductImeHistoryDto> {
        return namedParameterJdbcTemplate.query("""
        SELECT
            result.id, result.type, result.from_depot_id, result.to_depot_id, result.created_date, result.created_by, result.remarks
        FROM
                (
                    SELECT t1.id, '串码调拨' type, t1.from_depot_id, t1.to_depot_id, t1.created_date, t1.created_by, t1.remarks
                    FROM crm_ime_allot t1
                    WHERE t1.enabled = 1 AND t1.product_ime_id =:productImeId

                    UNION ALL

                    SELECT t2.business_id id, '货品订货' type, NULL from_depot_id, t2.shop_id to_depot_id, t1.created_date, t1.created_by, t1.remarks
                    FROM crm_goods_order_ime t1, crm_goods_order t2
                    WHERE t1.enabled = 1 AND t1.product_ime_id = :productImeId AND t1.goods_order_id = t2.id AND t2.enabled = 1

                    UNION ALL

                    SELECT t1.id, '售后调拨' type, t1.from_depot_id, t1.to_depot_id, t1.created_date, t1.created_by, t1.remarks
                    FROM crm_after_sale_ime_allot t1
                    WHERE t1.enabled = 1 AND t1.product_ime_id = :productImeId

                    UNION ALL

                    SELECT t2.id, '大库调拨' type, t2.from_store_id from_depot_id, t2.to_store_id to_depot_id, t1.created_date, t1.created_by, t1.remarks
                    FROM crm_store_allot_ime t1, crm_store_allot t2
                    WHERE t1.enabled = 1 AND t1.product_ime_id = :productImeId AND t1.store_allot_id = t2.id AND t2.enabled = 1

                    UNION ALL

                    SELECT t1.id, '串码上报' type, t1.shop_id from_depot_id, NULL to_depot_id, t1.created_date, t1.created_by, t1.remarks
                    FROM crm_product_ime_upload t1
                    WHERE t1.enabled = 1 AND t1.product_ime_id = :productImeId

                    UNION ALL

                    SELECT
                        t1.id,
                        (CASE WHEN t1.is_back = 1 THEN '核销退回' ELSE '串码核销' END) type,
                        (CASE WHEN t1.is_back = 1 THEN NULL ELSE t1.shop_id END ) from_depot_id,
                        (CASE WHEN t1.is_back = 1 THEN t1.shop_id ELSE NULL END ) to_depot_id,
                        t1.created_date,
                        t1.created_by,
                        t1.remarks
                    FROM crm_product_ime_sale t1
                    WHERE t1.enabled = 1 AND t1.product_ime_id = :productImeId

                ) result
        ORDER BY
            result.created_date DESC
                """, Collections.singletonMap("productImeId", productImeId), BeanPropertyRowMapper(ProductImeHistoryDto::class.java))
    }

    override fun findPage(pageable: Pageable, productImeQuery: ProductImeQuery): Page<ProductImeDto> {

        val sb = StringBuilder()
        sb.append("""
		SELECT
			sale.created_date productImeSaleCreatedDate,
			sale.employee_id productImeSaleEmployeeId,
			upload.created_date productImeUploadCreatedDate,
			upload.employee_id productImeUploadEmployeeId,
			product.product_type_id,
			depot.office_id depotOfficeId,
			t1.*
		FROM
			crm_product_ime t1
            LEFT JOIN crm_depot depot ON t1.depot_id = depot.id AND depot.enabled = 1
            LEFT JOIN crm_product product ON t1.product_id = product.id AND product.enabled = 1
            LEFT JOIN crm_product_ime_sale sale ON t1.product_ime_sale_id = sale.id AND sale.enabled = 1
            LEFT JOIN crm_product_ime_upload upload ON t1.product_ime_upload_id = upload.id AND upload.enabled = 1
		WHERE
			t1.enabled = 1
		    AND t1.company_id = :companyId
        """)
        if (StringUtils.isNotEmpty(productImeQuery.boxIme)) {
            sb.append("""  and t1.box_ime like CONCAT('%', :boxIme,'%') """)
        }
        if (StringUtils.isNotEmpty(productImeQuery.imeReverse)) {
            sb.append("""  and t1.ime_reverse like CONCAT( :imeReverse,'%') """)
        }
        if (StringUtils.isNotEmpty(productImeQuery.ime2)) {
            sb.append("""  and t1.ime2  like CONCAT('%', :ime2,'%') """)
        }
        if (StringUtils.isNotEmpty(productImeQuery.meid)) {
            sb.append("""  and t1.meid like CONCAT('%', :meid,'%')    """)
        }
        if (productImeQuery.imeOrMeidList != null) {
            sb.append("""  and (t1.ime in (:imeOrMeidList) or t1.ime2 in (:imeOrMeidList) or t1.meid in (:imeOrMeidList) ) """)
        }
        if (productImeQuery.createdDateStart != null) {
            sb.append("""  AND t1.created_date>= :createdDateStart """)
        }
        if (productImeQuery.createdDateEnd != null) {
            sb.append("""  AND t1.created_date < :createdDateEnd """)
        }
        if (productImeQuery.retailDateStart != null) {
            sb.append("""  AND t1.retail_date >= :retailDateStart  """)
        }
        if (productImeQuery.retailDateEnd != null) {
            sb.append("""  AND t1.retail_date <  :retailDateEnd  """)
        }
        if (productImeQuery.createdTimeStart != null) {
            sb.append("""  AND t1.created_time >= :createdTimeStart  """)
        }
        if (productImeQuery.createdTimeEnd != null) {
            sb.append("""  AND t1.created_time < :createdTimeEnd  """)
        }
        if (StringUtils.isNotBlank(productImeQuery.depotId )) {
            sb.append("""  and t1.depot_id = :depotId  """)
        }
        if (StringUtils.isNotBlank(productImeQuery.productId )) {
            sb.append("""  and t1.product_id = :productId  """)
        }
        if (StringUtils.isNotBlank(productImeQuery.inputType )) {
            sb.append("""  and t1.input_type = :inputType  """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val paramMap = BeanPropertySqlParameterSource(productImeQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ProductImeDto::class.java))
        return PageImpl(list,pageable,((pageable.pageNumber + 100) * pageable.pageSize).toLong())

    }


}