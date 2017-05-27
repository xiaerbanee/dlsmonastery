package net.myspring.cloud.modules.report.repository

import net.myspring.cloud.common.config.MyBeanPropertyRowMapper
import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto
import net.myspring.cloud.modules.report.web.query.CustomerReceiveDetailQuery
import net.myspring.common.dto.NameValueDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.util.*

/**
 * Created by haos on 2017/5/24.
 */
class  CustomerReceiveRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findEndShouldGet(dateEnd: LocalDate, customerIdList: MutableList<String>): MutableList<CustomerReceiveDto> {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateEnd", dateEnd)
        paramMap.put("customerIdList", customerIdList)
        return namedParameterJdbcTemplate.query("""
            SELECT
            temp.customerId,
            SUM (temp.endShouldGet) AS endShouldGet
            FROM
                (
            SELECT
                t2.FCONTACTUNIT AS customerId,
                t1.FAMOUNT AS endShouldGet
            FROM
                T_AR_OTHERRECABLEENTRY t1
                JOIN T_AR_OTHERRECABLE t2 ON t1.FID = t2.FID
            WHERE
                t2.FDATE <:dateEnd
                and t2.FCONTACTUNIT in  (:customerIdList)
                UNION ALL
            SELECT
                t2.FRETCUSTID AS customerId,
                - t3.FAMOUNT AS endShouldGet
                FROM
                T_SAL_RETURNSTOCKENTRY t1
                JOIN T_SAL_RETURNSTOCK t2 ON t1.FID = t2.FID
                JOIN T_SAL_RETURNSTOCKENTRY_F t3 ON t2.FID = t3.FID  AND t1.FENTRYID = t3.FENTRYID
                LEFT JOIN T_BAS_BILLTYPE_L t4 ON t4.FBILLTYPEID = t2.FBILLTYPEID
            WHERE
                t4.FNAME = '标准销售退货单'
                and t2.FDATE <:dateEnd
                and t2.FRETCUSTID in  (:customerIdList)
                UNION ALL
                SELECT
                t2.FCUSTOMERID AS customerId,
                t3.FALLAMOUNT AS endShouldGet
            FROM
                T_SAL_OUTSTOCKENTRY t1
                JOIN T_SAL_OUTSTOCK t2 ON t1.FID = t2.FID
                JOIN T_SAL_OUTSTOCKENTRY_F t3 ON t1.FID = t3.FID AND t1.FENTRYID = t3.FENTRYID
                LEFT JOIN T_BAS_BILLTYPE_L t4 ON t2.FBILLTYPEID = t4.FBILLTYPEID
             WHERE
                t4.FNAME = '标准销售出库单'
                and t2.FDATE <:dateEnd
                and t2.FCUSTOMERID in :customerIdList
                UNION ALL
             SELECT
                t2.FCONTACTUNIT AS customerId,
                - t1.FRECAMOUNT_E AS endShouldGet
             FROM
                T_AR_RECEIVEBILLENTRY t1
                JOIN T_AR_RECEIVEBILL t2 ON t1.FID = t2.FID
             WHERE
                t2.FDATE <:dateEnd
                and t2.FCONTACTUNIT in (:customerIdList)
                UNION ALL
             SELECT
                t2.FCONTACTUNIT AS customerId,
                t1.FREALREFUNDAMOUNTFOR AS endShouldGet
            FROM
                T_AR_REFUNDBILLENTRY t1
                JOIN T_AR_REFUNDBILL t2 ON t2.FID = t1.FID
             WHERE
                t2.FDATE <:dateEnd
                and t2.FCONTACTUNIT in (:customerIdList)
                ) temp
                GROUP BY
                temp.customerId
        """, paramMap, MyBeanPropertyRowMapper(CustomerReceiveDto::class.java))
    }

    fun findMainList(customerReceiveDetailQuery: CustomerReceiveDetailQuery): MutableList<CustomerReceiveDetailDto> {
        var paramMap = BeanPropertySqlParameterSource(customerReceiveDetailQuery)
        return namedParameterJdbcTemplate.query("""
            SELECT
            t.id,
            t.customerId,
            t.billType,
            t.billNo,
            t.date as billDate,
            t.totalAmount,
            t. STATUS as billStatus
            FROM (
            SELECT
            t01.FID AS id,
            t01.FCONTACTUNIT AS customerId,
            t02.FNAME AS billType,
            t01.FBillNo AS billNo,
            t01.FDATE AS date,
            t01.FAMOUNTFOR AS totalAmount,
            t01.FDocumentStatus AS STATUS
            FROM
            T_AR_OTHERRECABLE t01,
            T_BAS_BILLTYPE_L t02
            WHERE
            t01.FBILLTYPEID = t02.FBILLTYPEID
            AND t01.FCONTACTUNIT IN (:customerIdList)

            AND t01.FDATE >=:dateStart
            AND t01.FDATE <:dateEnd

            UNION ALL
            SELECT
            t11.FID AS id,
            t11.FRETCUSTID AS customerId,
            t12.FNAME AS billType,
            t11.FBillNo AS billNo,
            t11.FDATE AS date,
            sum(t13.FAMOUNT) AS totalAmount,
            t11.FDOCUMENTSTATUS AS STATUS
            FROM
            T_SAL_RETURNSTOCK t11,
            T_BAS_BILLTYPE_L t12,
            T_SAL_RETURNSTOCKENTRY_F t13
            WHERE
            t11.FID = t13.FID
            AND t11.FBILLTYPEID = t12.FBILLTYPEID
            AND t12.FNAME = '标准销售退货单'
            AND t11.FRETCUSTID IN (:customerIdList)
            AND t11.FDATE >=:dateStart
            AND t11.FDATE <:dateEnd GROUP BY t11.FID,
            t11.FRETCUSTID,
            t12.FNAME,
            t11.FBillNo,
            t11.FDATE,
            t11.FDOCUMENTSTATUS

            UNION ALL
            SELECT
            t21.FID AS id,
            t21.FCONTACTUNIT AS customerId,
            t22.FNAME AS billType,
            t21.FBillNo AS billNo,
            t21.FDATE AS date,
            t21.FRECEIVEAMOUNTFOR AS totalAmount,
            t21.FDocumentStatus AS STATUS
            FROM
            T_AR_RECEIVEBILL t21,
            T_BAS_BILLTYPE_L t22
            WHERE
            t21.FBILLTYPEID = t22.FBILLTYPEID
            AND t21.FCONTACTUNIT IN (:customerIdList)
            AND t21.FDATE >=:dateStart
            AND t21.FDATE <:dateEnd

             UNION ALL
             SELECT
             t31.FID AS id,
            t31.FCONTACTUNIT AS customerId,
            t32.FNAME AS billType,
            t31.FBillNo AS billNo,
            t31.FDATE AS date,
            t31.FREALREFUNDAMOUNTFOR AS totalAmount,
            t31.FDocumentStatus AS STATUS
            FROM
            T_AR_REFUNDBILL t31,
            T_BAS_BILLTYPE_L t32
            WHERE
            t31.FBILLTYPEID = t32.FBILLTYPEID
            AND t31.FCONTACTUNIT IN (:customerIdList)
            AND t31.FDATE >=:dateStart
            AND t31.FDATE <:dateEnd

             UNION ALL
             SELECT
             t41.FID AS id,
            t41.FCUSTOMERID AS customerId,
            t42.FNAME AS billType,
            t41.FBillNo AS billNo,
            t41.FDATE AS date,
            sum(t43.FAMOUNT) AS totalAmount,
            t41.FDOCUMENTSTATUS AS STATUS
            FROM
            T_SAL_OUTSTOCK t41,
            T_BAS_BILLTYPE_L t42,
            T_SAL_OUTSTOCKENTRY_F t43
            WHERE
            t41.FID = t43.FID
            AND t41.FBILLTYPEID = t42.FBILLTYPEID
            AND t42.FNAME = '标准销售出库单'
            AND t41.FCUSTOMERID IN (:customerIdList)
            AND t41.FDATE >=:dateStart
            AND t41.FDATE <:dateEnd GROUP BY  t41.FID,
            t41.FCUSTOMERID,
            t42.FNAME,
            t41.FBillNo,
            t41.FDATE,
            t41.FDOCUMENTSTATUS
            UNION ALL
            SELECT
            t51.FID AS id,
            t51.FRETCUSTID AS customerId,
            t52.FNAME AS billType,
            t51.FBillNo AS billNo,
            t51.FDATE AS date,
            sum(t53.FAMOUNT) AS totalAmount,
            t51.FDOCUMENTSTATUS AS STATUS
            FROM
            T_SAL_RETURNSTOCK t51,
            T_BAS_BILLTYPE_L t52,
            T_SAL_RETURNSTOCKENTRY_F t53
            WHERE
            t51.FID = t53.FID
            AND t51.FBILLTYPEID = t52.FBILLTYPEID
            AND t52.FNAME = '现销退货单'
            AND t51.FRETCUSTID IN (:customerIdList)
            AND t51.FDATE >=:dateStart
            AND t51.FDATE <:dateEnd GROUP BY t51.FID,
            t51.FRETCUSTID,
            t52.FNAME,
            t51.FBillNo,
            t51.FDATE,
            t51.FDOCUMENTSTATUS
            UNION ALL
            SELECT
            t61.FID AS id,
            t61.FCUSTOMERID AS customerId,
            t62.FNAME AS billType,
            t61.FBillNo AS billNo,
            t61.FDATE AS date,
            sum(t63.FAMOUNT) AS totalAmount,
            t61.FDOCUMENTSTATUS AS STATUS
            FROM
            T_SAL_OUTSTOCK t61,
            T_BAS_BILLTYPE_L t62,
            T_SAL_OUTSTOCKENTRY_F t63
            WHERE
            t61.FID = t63.FID
            AND t61.FBILLTYPEID = t62.FBILLTYPEID
            AND t62.FNAME = '现销出库单'
            AND t61.FCUSTOMERID IN (:customerIdList)
            AND t61.FDATE >=:dateStart
            AND t61.FDATE <:dateEnd  GROUP BY  t61.FID,
            t61.FCUSTOMERID,
            t62.FNAME,
            t61.FBillNo,
            t61.FDATE,
            t61.FDOCUMENTSTATUS
            ) t
            ORDER BY
            t.customerId,
            t.date,
            t.billNo
        """, paramMap, MyBeanPropertyRowMapper(CustomerReceiveDetailDto::class.java))

    }

    fun findDetailList(customerReceiveDetailQuery: CustomerReceiveDetailQuery): MutableList<CustomerReceiveDetailDto> {
        var paramMap = BeanPropertySqlParameterSource(customerReceiveDetailQuery)
        return namedParameterJdbcTemplate.query("""
            SELECT
            t11.FID AS id,
            t11.FBillNo AS billNo,
            t11.FCUSTOMERID AS customerId,
            t13.FNAME AS materialName,
            t12.FREALQTY AS qty,
            t14.FPRICE AS price,
            t14.FALLAMOUNT AS totalAmount,
            t12.FNOTE AS remarks
            FROM
            T_SAL_OUTSTOCK t11,
            T_SAL_OUTSTOCKENTRY t12,
            T_BD_MATERIAL_L t13,
            T_SAL_OUTSTOCKENTRY_F t14
            WHERE
            t11.FID = t12.FID
            AND t12.FENTRYID = t14.FENTRYID
            AND t12.FMATERIALID = t13.FMATERIALID
            AND t11.FCUSTOMERID IN (:customerIdList)
            AND t11.FDATE >:dateStart
            AND t11.FDATE <:dateEnd

            UNION ALL
            SELECT
            t21.FID AS id,
            t21.FBillNo AS billNo,
            t21.FRETCUSTID AS customerId,
            t23.FNAME AS materialName,
            t22.FREALQTY AS qty,
            t24.FPRICE AS price,
            t24.FAMOUNT AS totalAmount,
            t22.FNOTE AS remarks
            FROM
            T_SAL_RETURNSTOCK t21,
            T_SAL_RETURNSTOCKENTRY t22,
            T_BD_MATERIAL_L t23,
            T_SAL_RETURNSTOCKENTRY_F t24
            WHERE
            t21.FID = t22.FID
            AND t22.FENTRYID = t24.FENTRYID
            AND t22.FMATERIALID = t23.FMATERIALID
            AND t21.FRETCUSTID IN (:customerIdList)
            AND t21.FDATE >=:dateStart
            AND t21.FDATE <:dateEnd
        """, paramMap, MyBeanPropertyRowMapper(CustomerReceiveDetailDto::class.java))
    }

    fun findRemarks(customerReceiveDetailQuery: CustomerReceiveDetailQuery): MutableList<NameValueDto> {
        var paramMap = BeanPropertySqlParameterSource(customerReceiveDetailQuery)
        return namedParameterJdbcTemplate.query("""
            SELECT
            t1.FBILLNO AS name,
            t2.FCOMMENT AS value
            FROM
            T_AR_OTHERRECABLE t1,
            T_AR_OTHERRECABLEENTRY t2
            WHERE
            t1.FID = t2.FID
            AND t1.FCONTACTUNIT IN (:customerIdList)
            AND t1.FDATE  >:dateStart
            AND t1.FDATE <:dateEnd
            UNION ALL
            SELECT
            t3.FBILLNO AS name,
            t4.FCOMMENT AS value
            FROM
            T_AR_RECEIVEBILL t3,
            T_AR_RECEIVEBILLENTRY t4
            WHERE
            t3.FID = t4.FID
            AND t3.FCONTACTUNIT  IN (:customerIdList)
            AND t3.FDATE  >:#{dateStart}
            AND t3.FDATE <:#{dateEnd}
            UNION ALL
            SELECT
            t5.FBILLNO AS name,
            t6.FNOTE AS value
            FROM
            T_AR_REFUNDBILL t5,
            T_AR_REFUNDBILLENTRY t6
            WHERE
            t5.FID = t6.FID
            AND t5.FCONTACTUNIT IN (:customerIdList)
            AND t5.FDATE  >:dateStart
            AND t5.FDATE <:#dateEnd
        """, paramMap, MyBeanPropertyRowMapper(NameValueDto::class.java))

    }
}