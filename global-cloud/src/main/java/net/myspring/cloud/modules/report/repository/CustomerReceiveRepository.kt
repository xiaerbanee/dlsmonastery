package net.myspring.cloud.modules.report.repository

import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto
import net.myspring.cloud.modules.report.web.query.CustomerReceiveDetailQuery
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery
import net.myspring.common.dto.NameValueDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

/**
 * Created by haos on 2017/5/24.
 */
@Component
class  CustomerReceiveRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    //截止结余:sum(其他应收-标准销售退货单-收款单+收款退款单+标准销售出库单-现销退货单+现销出库单)
    fun findEndGet(dateEnd: LocalDate, customerIdList: MutableList<String>): MutableList<CustomerReceiveDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateEnd", dateEnd.toString())
        paramMap.put("customerIdList", customerIdList)
        return namedParameterJdbcTemplate.query("""
            SELECT
            temp.customerId,
            isnull(SUM(temp.endShouldGet),0) AS endShouldGet
            FROM
                (
                SELECT
                    t2.FCONTACTUNIT AS customerId,
                    t1.FAMOUNT AS endShouldGet
                FROM
                    T_AR_OTHERRECABLEENTRY t1
                    JOIN T_AR_OTHERRECABLE t2 ON t1.FID = t2.FID
                WHERE
                    t2.FDATE < :dateEnd
                    and t2.FCONTACTUNIT in  (:customerIdList)
                UNION ALL
                SELECT
                    t2.FRETCUSTID AS customerId,
                    - t3.FAMOUNT AS endShouldGet
                    FROM
                    T_SAL_RETURNSTOCKENTRY t1
                    JOIN T_SAL_RETURNSTOCK t2 ON t1.FID = t2.FID
                    JOIN T_SAL_RETURNSTOCKENTRY_F t3 ON t2.FID = t3.FID  and t1.FENTRYID = t3.FENTRYID
                    LEFT JOIN T_BAS_BILLTYPE_L t4 ON t4.FBILLTYPEID = t2.FBILLTYPEID
                WHERE
                    t4.FNAME = '标准销售退货单'
                    and t2.FDATE < :dateEnd
                    and t2.FRETCUSTID in  (:customerIdList)
                UNION ALL
                SELECT
                    t2.FCUSTOMERID AS customerId,
                    t3.FALLAMOUNT AS endShouldGet
                FROM
                    T_SAL_OUTSTOCKENTRY t1
                    JOIN T_SAL_OUTSTOCK t2 ON t1.FID = t2.FID
                    JOIN T_SAL_OUTSTOCKENTRY_F t3 ON t1.FID = t3.FID and t1.FENTRYID = t3.FENTRYID
                    LEFT JOIN T_BAS_BILLTYPE_L t4 ON t2.FBILLTYPEID = t4.FBILLTYPEID
                WHERE
                    t4.FNAME = '标准销售出库单'
                    and t2.FDATE < :dateEnd
                    and t2.FCUSTOMERID in (:customerIdList)
                UNION ALL
                 SELECT
                    t2.FCONTACTUNIT AS customerId,
                    - t1.FRECAMOUNT_E AS endShouldGet
                 FROM
                    T_AR_RECEIVEBILLENTRY t1
                    JOIN T_AR_RECEIVEBILL t2 ON t1.FID = t2.FID
                 WHERE
                    t2.FDATE < :dateEnd
                    and t2.FCONTACTUNIT in (:customerIdList)
                UNION ALL
                 SELECT
                    t2.FCONTACTUNIT AS customerId,
                    t1.FREALREFUNDAMOUNTFOR AS endShouldGet
                 FROM
                    T_AR_REFUNDBILLENTRY t1
                    JOIN T_AR_REFUNDBILL t2 ON t2.FID = t1.FID
                 WHERE
                    t2.FDATE < :dateEnd
                    and t2.FCONTACTUNIT in (:customerIdList)
            ) temp
            GROUP BY
            temp.customerId
        """, paramMap, BeanPropertyRowMapper(CustomerReceiveDto::class.java))
    }

    //应收金额：sum(其他应收-标准销售退货单+标准销售出库单)
    fun findShouldGet(customerReceiveQuery:CustomerReceiveQuery): MutableList<CustomerReceiveDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", customerReceiveQuery.dateStart.toString())
        paramMap.put("dateEnd", customerReceiveQuery.dateEnd.toString())
        paramMap.put("customerIdList", customerReceiveQuery.customerIdList)
        var sb = StringBuilder("""
            SELECT
                t.customerId AS customerId,
                SUM (t.beginAmount) AS shouldGet
            FROM
            (
            """)
        //其他应收
        sb.append("""
            SELECT
                b.FCONTACTUNIT AS customerId,
                a.FAMOUNT AS beginAmount
            FROM
              T_AR_OTHERRECABLEENTRY a
            JOIN T_AR_OTHERRECABLE b ON a.FID = b.FID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE  <= :dateEnd
                and b.FCONTACTUNIT in (:customerIdList)
                """)
        //-标准销售退货单
        sb.append("""
            union all
                SELECT
                    b.FRETCUSTID AS customerId,
                    -c.FAMOUNT AS beginAmount
                FROM
                    T_SAL_RETURNSTOCKENTRY a
                    JOIN T_SAL_RETURNSTOCK b ON a.FID = b.FID
                    JOIN T_SAL_RETURNSTOCKENTRY_F c ON b.FID = c.FID and a.FENTRYID = c.FENTRYID
                    LEFT JOIN T_BAS_BILLTYPE_L d ON d.FBILLTYPEID = b.FBILLTYPEID
                WHERE
                    b.FDATE >= :dateStart 
                    and b.FDATE  <= :dateEnd
                    and d.FNAME= '标准销售退货单'
                    and b.FRETCUSTID in (:customerIdList)
                """)
        //标准销售出库单
        sb.append("""
            union all
                SELECT
                    b.FCUSTOMERID AS customerId,
                    c.FALLAMOUNT AS beginAmount
                FROM
                    T_SAL_OUTSTOCKENTRY a
                    JOIN T_SAL_OUTSTOCK b ON a.FID = b.FID
                    JOIN T_SAL_OUTSTOCKENTRY_F c ON a.FID = c.FID and a.FENTRYID = c.FENTRYID
                    LEFT JOIN T_BAS_BILLTYPE_L d ON b.FBILLTYPEID = d.FBILLTYPEID
                WHERE
                    b.FDATE >= :dateStart 
                    and b.FDATE  <= :dateEnd
                    and d.FNAME= '标准销售出库单'
                    and b.FCUSTOMERID in (:customerIdList)
                """)
        sb.append("""
                ) t
              GROUP BY
              t.customerId
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(CustomerReceiveDto::class.java))
    }

    //实收金额：收款单-收款退款单
    fun findActualGet(customerReceiveQuery:CustomerReceiveQuery): MutableList<CustomerReceiveDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", customerReceiveQuery.dateStart.toString())
        paramMap.put("dateEnd", customerReceiveQuery.dateEnd.toString())
        paramMap.put("customerIdList", customerReceiveQuery.customerIdList)
        var sb = StringBuilder("""
            SELECT
                t.customerId AS customerId,
                SUM (t.beginAmount) AS realGet
            FROM
            (
                """)
        //收款单
        sb.append("""
            SELECT
                b.FCONTACTUNIT AS customerId,
                a.FRECAMOUNT_E AS beginAmount
            FROM
                T_AR_RECEIVEBILLENTRY a
                join T_AR_RECEIVEBILL b on a.FID = b.FID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE  <= :dateEnd
                and b.FCONTACTUNIT in (:customerIdList)
                """)

        //-收款退款单
        sb.append("""
            union all
                SELECT
                    b.FCONTACTUNIT AS customerId,
                    -a.FREALREFUNDAMOUNTFOR AS beginAmount
                FROM
                    T_AR_REFUNDBILLENTRY a
                    JOIN T_AR_REFUNDBILL b ON b.FID = a.FID
                WHERE
                    b.FDATE >= :dateStart
                    and b.FDATE  <= :dateEnd
                    and b.FCONTACTUNIT in (:customerIdList)
                """)
        sb.append("""
                ) t
              GROUP BY
              t.customerId
        """);
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(CustomerReceiveDto::class.java))
    }

    //其他应收-sum标准销售退货单-收款单+收款退款单+sum标准销售出库单-sum现销退货单+sum现销出库单
    fun findMainList(customerReceiveDetailQuery: CustomerReceiveDetailQuery): MutableList<CustomerReceiveDetailDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart",customerReceiveDetailQuery.dateStart.toString())
        paramMap.put("dateEnd",customerReceiveDetailQuery.dateEnd.toString())
        paramMap.put("customerIdList",customerReceiveDetailQuery.customerIdList)
        return namedParameterJdbcTemplate.query("""
            SELECT
                t.id,
                t.customerId,
                t.billType,
                t.billNo,
                t.date as billDate,
                isnull(t.totalAmount,0) as totalAmount,
                t.STATUS as billStatus
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
                    and t01.FCONTACTUNIT IN (:customerIdList)

                    and t01.FDATE >=:dateStart
                    and t01.FDATE <=:dateEnd

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
                    and t11.FBILLTYPEID = t12.FBILLTYPEID
                    and t12.FNAME = '标准销售退货单'
                    and t11.FRETCUSTID IN (:customerIdList)
                    and t11.FDATE >=:dateStart
                    and t11.FDATE <=:dateEnd
                GROUP BY t11.FID,
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
                    and t21.FCONTACTUNIT IN (:customerIdList)
                    and t21.FDATE >=:dateStart
                    and t21.FDATE <=:dateEnd

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
                    and t31.FCONTACTUNIT IN (:customerIdList)
                    and t31.FDATE >=:dateStart
                    and t31.FDATE <=:dateEnd

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
                    and t41.FBILLTYPEID = t42.FBILLTYPEID
                    and t42.FNAME = '标准销售出库单'
                    and t41.FCUSTOMERID IN (:customerIdList)
                    and t41.FDATE >=:dateStart
                    and t41.FDATE <=:dateEnd
                GROUP BY  t41.FID,
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
                    and t51.FBILLTYPEID = t52.FBILLTYPEID
                    and t52.FNAME = '现销退货单'
                    and t51.FRETCUSTID IN (:customerIdList)
                    and t51.FDATE >=:dateStart
                    and t51.FDATE <=:dateEnd
                GROUP BY t51.FID,
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
                    and t61.FBILLTYPEID = t62.FBILLTYPEID
                    and t62.FNAME = '现销出库单'
                    and t61.FCUSTOMERID IN (:customerIdList)
                    and t61.FDATE >=:dateStart
                    and t61.FDATE <=:dateEnd
                GROUP BY  t61.FID,
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
        """, paramMap, BeanPropertyRowMapper(CustomerReceiveDetailDto::class.java))

    }

    //显示物料:标准销售出库单+现销退货单
    fun findDetailList(customerReceiveDetailQuery: CustomerReceiveDetailQuery): MutableList<CustomerReceiveDetailDto> {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart",customerReceiveDetailQuery.dateStart.toString())
        paramMap.put("dateEnd",customerReceiveDetailQuery.dateEnd.toString())
        paramMap.put("customerIdList",customerReceiveDetailQuery.customerIdList)
        return namedParameterJdbcTemplate.query("""
            SELECT
                t11.FID AS id,
                t11.FBillNo AS billNo,
                t11.FCUSTOMERID AS customerId,
                t13.FNAME AS materialName,
                isnull(t12.FREALQTY,0) AS qty,
                isnull(t14.FPRICE,0) AS price,
                isnull(t14.FALLAMOUNT,0) AS totalAmount,
                t12.FNOTE AS remarks,
                t11.FDOCUMENTSTATUS as billStatus
            FROM
                T_SAL_OUTSTOCK t11,
                T_SAL_OUTSTOCKENTRY t12,
                T_BD_MATERIAL_L t13,
                T_SAL_OUTSTOCKENTRY_F t14
            WHERE
                t11.FID = t12.FID
                and t12.FENTRYID = t14.FENTRYID
                and t12.FMATERIALID = t13.FMATERIALID
                and t11.FCUSTOMERID IN (:customerIdList)
                and t11.FDATE >:dateStart
                and t11.FDATE <:dateEnd

            UNION ALL
            SELECT
                t21.FID AS id,
                t21.FBillNo AS billNo,
                t21.FRETCUSTID AS customerId,
                t23.FNAME AS materialName,
                isnull(t22.FREALQTY,0) AS qty,
                isnull(t24.FPRICE,0) AS price,
                isnull(t24.FAMOUNT,0) AS totalAmount,
                t22.FNOTE AS remarks,
                t21.FDOCUMENTSTATUS as billStatus
            FROM
                T_SAL_RETURNSTOCK t21,
                T_SAL_RETURNSTOCKENTRY t22,
                T_BD_MATERIAL_L t23,
                T_SAL_RETURNSTOCKENTRY_F t24
            WHERE
                t21.FID = t22.FID
                and t22.FENTRYID = t24.FENTRYID
                and t22.FMATERIALID = t23.FMATERIALID
                and t21.FRETCUSTID IN (:customerIdList)
                and t21.FDATE >=:dateStart
                and t21.FDATE <:dateEnd
        """, paramMap, BeanPropertyRowMapper(CustomerReceiveDetailDto::class.java))
    }

    //显示备注:其他应收+收款单+收款退款单
    fun findRemarks(customerReceiveDetailQuery: CustomerReceiveDetailQuery): MutableList<NameValueDto> {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart",customerReceiveDetailQuery.dateStart.toString())
        paramMap.put("dateEnd",customerReceiveDetailQuery.dateEnd.toString())
        paramMap.put("customerIdList",customerReceiveDetailQuery.customerIdList)
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FBILLNO AS name,
                t2.FCOMMENT AS value
            FROM
                T_AR_OTHERRECABLE t1,
                T_AR_OTHERRECABLEENTRY t2
            WHERE
                t1.FID = t2.FID
                and t1.FCONTACTUNIT IN (:customerIdList)
                and t1.FDATE  >:dateStart
                and t1.FDATE <:dateEnd
            UNION ALL
            SELECT
                t3.FBILLNO AS name,
                t4.FCOMMENT AS value
            FROM
                T_AR_RECEIVEBILL t3,
                T_AR_RECEIVEBILLENTRY t4
            WHERE
                t3.FID = t4.FID
                and t3.FCONTACTUNIT  IN (:customerIdList)
                and t3.FDATE  >:dateStart
                and t3.FDATE <:dateEnd
            UNION ALL
            SELECT
                t5.FBILLNO AS name,
                t6.FNOTE AS value
            FROM
                T_AR_REFUNDBILL t5,
                T_AR_REFUNDBILLENTRY t6
            WHERE
                t5.FID = t6.FID
                and t5.FCONTACTUNIT IN (:customerIdList)
                and t5.FDATE  >:dateStart
                and t5.FDATE <:dateEnd
        """, paramMap, BeanPropertyRowMapper(NameValueDto::class.java))

    }
}