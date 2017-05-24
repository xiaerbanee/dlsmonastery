package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.*
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.web.query.BankInQuery
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import net.myspring.future.modules.crm.web.query.StoreAllotQuery
import net.myspring.future.modules.crm.dto.StoreAllotImeDto
import net.myspring.future.modules.crm.dto.StoreAllotDto
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import net.myspring.common.dto.NameValueDto
import net.myspring.future.modules.crm.domain.ProductImeSale
import net.myspring.future.modules.crm.domain.ProductIme
import net.myspring.future.modules.crm.dto.ProductImeHistoryDto
import net.myspring.future.modules.crm.dto.ProductImeDto
import net.myspring.future.modules.crm.web.query.ProductImeQuery
import net.myspring.future.modules.crm.web.query.ProductImeShipQuery






interface ProductImeRepository : BaseRepository<ProductIme, String> {

    fun findByIme(ime: String): ProductIme

    @Query("""
    SELECT
        t1.*
    FROM
        crm_product_ime t1
    WHERE
        t1.enabled = 1
        AND (
            t1.ime IN ? 1
            OR t1.ime2 IN ? 1
            OR t1.meid IN ? 1
        )
        """, nativeQuery = true)
    fun findByImeList(imeList: List<String>): List<ProductIme>

    @Query("""
        SELECT
            sale.created_date productImeSaleCreatedDate,
            sale.created_by productImeSaleCreatedBy,
            sale.employee_id productImeSaleEmployeeId,
            sale.shop_id productImeSaleShopId,
            upload.created_date productImeUploadCreatedDate,
            upload.created_by productImeUploadCreatedBy,
            upload.employee_id productImeUploadEmployeeId,
            upload.shop_id productImeUploadShopId,
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
                AND t1.ime in :imeList
            ) validProductIme
            LEFT JOIN crm_product_ime_sale sale ON validProductIme.product_ime_sale_id = sale.id
            LEFT JOIN crm_product_ime_upload upload ON validProductIme.product_ime_upload_id = upload.id
        """, nativeQuery = true)
    fun findDtoListByImeList(@Param("imeList") imeList: List<String>, @Param("companyId") companyId: String): List<ProductImeDto>

    @Query("""
    SELECT
        t1.*
    FROM
        crm_product_ime t1
    WHERE
        t1.enabled = 1
        AND t1.depot_id = : #{#p.depotId}
        AND t1.box_ime IN : #{#p.boxImeList}
    UNION
        SELECT
            t1.*
        FROM
            crm_product_ime t1
        WHERE
            t1.enabled = 1
            AND t1.depot_id =: #{#p.depotId}
            AND (
                t1.ime IN : #{#p.imeList}
                OR t1.ime2 IN : #{#p.imeList}
                OR t1.meid IN : #{#p.imeList}
            )
        """, nativeQuery = true)
    //TODO 调用时，要确保传入的参数里的list不为null
    fun findShipList(@Param("p") productImeShipQuery: ProductImeShipQuery): List<ProductIme>

    @Query("""
    SELECT
        sale.created_date productImeSaleCreatedDate,
        upload.created_date productImeUploadCreatedDate,
        ime.*
    FROM
        crm_product_ime ime
        LEFT JOIN crm_product_ime_sale sale ON ime.product_ime_sale_id = sale.id
        LEFT JOIN crm_product_ime_upload upload ON ime.product_ime_upload_id = upload.id
    WHERE
        ime.enabled = 1
        AND ime.id = ?1
        """, nativeQuery = true)
    fun findProductImeDto(productImeId: String): ProductImeDto


    @Query("""
       SELECT
            result.id, result.type, result.from_depot_id, result.to_depot_id, result.created_date, result.created_by, result.remarks
        FROM
                (
                    SELECT t1.id, '串码调拨' type, t1.from_depot_id, t1.to_depot_id, t1.created_date, t1.created_by, t1.remarks
                    FROM crm_ime_allot t1
                    WHERE t1.enabled = 1 AND t1.product_ime_id = ?1

                    UNION ALL

                    SELECT t2.business_id id, '货品订货' type, NULL from_depot_id, t2.shop_id to_depot_id, t1.created_date, t1.created_by, t1.remarks
                    FROM crm_goods_order_ime t1, crm_goods_order t2
                    WHERE t1.enabled = 1 AND t1.product_ime_id = ?1 AND t1.goods_order_id = t2.id AND t2.enabled = 1

                    UNION ALL

                    SELECT t1.id, '售后调拨' type, t1.from_depot_id, t1.to_depot_id, t1.created_date, t1.created_by, t1.remarks
                    FROM crm_after_sale_ime_allot t1
                    WHERE t1.enabled = 1 AND t1.product_ime_id = ?1

                    UNION ALL

                    SELECT t2.id, '大库调拨' type, t2.from_store_id from_depot_id, t2.to_store_id to_depot_id, t1.created_date, t1.created_by, t1.remarks
                    FROM crm_store_allot_ime t1, crm_store_allot t2
                    WHERE t1.enabled = 1 AND t1.product_ime_id = ?1 AND t1.store_allot_id = t2.id AND t2.enabled = 1

                    UNION ALL

                    SELECT t1.id, '串码上报' type, t1.shop_id from_depot_id, NULL to_depot_id, t1.created_date, t1.created_by, t1.remarks
                    FROM crm_product_ime_upload t1
                    WHERE t1.enabled = 1 AND t1.product_ime_id = ?1

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
                    WHERE t1.enabled = 1 AND t1.product_ime_id = ?1

                ) result
        ORDER BY
            result.created_date DESC
        """, nativeQuery = true)
    fun findProductImeHistoryList(productImeId: String): List<ProductImeHistoryDto>



    @Query("""
    SELECT
        t1.*
    FROM
        crm_product_ime t1
    where t1.enabled=1
        AND t1.depot_id = :depotId
        AND t1.ime_reverse LIKE CONCAT( :imeReverse,'%')
    Limit 20
        """, nativeQuery = true)
    fun findByImeReverseLike(@Param("imeReverse") ime: String, @Param("depotId") depotId: String): List<ProductIme>

}
