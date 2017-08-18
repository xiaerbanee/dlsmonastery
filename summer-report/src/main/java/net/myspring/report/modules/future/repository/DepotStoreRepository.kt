package net.myspring.report.modules.future.repository
import net.myspring.report.modules.future.dto.DepotReportDto
import net.myspring.report.modules.future.dto.DepotStoreDto
import net.myspring.report.modules.future.web.query.DepotStoreQuery
import net.myspring.report.modules.future.web.query.ReportQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class DepotStoreRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findStoreReport(reportQuery: ReportQuery): MutableList<DepotReportDto> {
        val sb = StringBuffer()
        if(reportQuery.isDetail==null||!reportQuery.isDetail){
            sb.append("""   SELECT t4.id as depotId,t4.area_id,t4.office_id,t4.name as 'depotName'',t2.name as 'productName', COUNT(t1.id) AS qty""")
        }else if(reportQuery.isDetail){
            sb.append("""
               SELECT t2.id as 'productId',t2.name as 'productName',t1.ime,t4.name as 'depotName'
            """)
        }
        sb.append("""
                    FROM
                    crm_product_ime t1
                    LEFT JOIN crm_product_ime_upload t6 ON t1.product_ime_upload_id = t6.id
                    LEFT JOIN crm_product t2 on t1.product_id=t2.id
                    LEFT JOIN crm_product_type t3 on t2.product_type_id=t3.id
                    LEFT JOIN crm_depot t4 on t1.depot_id=t4.id,
                    crm_depot_store t5
                    WHERE
                    t1.enabled = 1
                    and  t4.depot_store_id=t5.id
        """)
        if(reportQuery.date!=null){
            sb.append("""
                AND (
                    t1.retail_date IS NULL
                    OR t1.retail_date >:date
                )
                AND (
                    t6.id IS NULL
                    OR t6.created_date > :date
                )
                AND t1.created_date < :date
            """)
        }
        if(reportQuery.scoreType!=null){
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
        if(reportQuery.isDetail!=null&&reportQuery.isDetail){
            sb.append(""" group by t1.depot_id,t2.id""")
        }
        if(StringUtils.isNotBlank(reportQuery.exportType)&&reportQuery.exportType=="按合计"){
            sb.append(""" group by t1.depot_id""")
        }
        print(sb.toString())
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(reportQuery), BeanPropertyRowMapper(DepotReportDto::class.java))
    }

    fun findFilter(depotStoreQuery: DepotStoreQuery): MutableList<DepotStoreDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t2.*, t1.id AS 'depotId',
                t1.name as 'depotName',
                t1.office_id AS 'officeId',
                t1.tax_name AS 'taxName',
                t1.delegate_depot_id AS 'delegateDepotId',
                depot.name as 'delegateDepotName',
                t1.pop_shop AS 'popShop',
                t1.area_id,t1.contator,t1.mobile_phone
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
        if (StringUtils.isNotEmpty(depotStoreQuery.contator)) {
            sb.append("""  and t1.contator LIKE CONCAT('%',:contator,'%') """)
        }
        if (StringUtils.isNotEmpty(depotStoreQuery.mobilePhone)) {
            sb.append("""  and t1.mobile_phone LIKE CONCAT('%',:mobilePhone,'%') """)
        }
        if (StringUtils.isNotEmpty(depotStoreQuery.depotId)) {
            sb.append(""" and t1.id = :depotId """)
        }

        if (StringUtils.isNotEmpty(depotStoreQuery.officeId)) {
            sb.append("""  and t1.office_id =:officeId """)
        }
        if (StringUtils.isNotEmpty(depotStoreQuery.areaId)) {
            sb.append("""  and t1.area_id =:areaId """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(depotStoreQuery), BeanPropertyRowMapper(DepotStoreDto::class.java))
    }
}