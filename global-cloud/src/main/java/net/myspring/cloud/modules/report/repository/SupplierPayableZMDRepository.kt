package net.myspring.cloud.modules.report.repository

import net.myspring.cloud.modules.report.dto.SupplierPayableDetailDto
import net.myspring.cloud.modules.report.dto.SupplierPayableDto
import net.myspring.cloud.modules.report.web.query.SupplierPayableQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.HashMap

/**
 * 供应商-应付(ZMD)
 * Created by lihx on 2017/6/29.
 */
@Component
class SupplierPayableZMDRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    //截止结余：sum(采购入库单-采购退料单+应付单+其他应付单-付款单+付款退款单)
    fun findEndPayable(dateEnd: LocalDate, supplierIdList: MutableList<String>,departmentIdList: MutableList<String>): MutableList<SupplierPayableDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateEnd", dateEnd.toString())
        paramMap.put("supplierIdList", supplierIdList)
        paramMap.put("departmentIdList",departmentIdList)
        var sb = StringBuilder("""
            SELECT
                a.supplierId AS supplierId,
                a.departmentId AS departmentId,
                SUM (a.fsamount) AS beginAmount
            FROM
            (
            """)
                //采购入库单
                sb.append("""
                    SELECT
                        b.FSUPPLIERID AS supplierId,
                        b.fpurchaseDeptId AS departmentId,
                        c.FALLAMOUNT AS fsamount
                    FROM
                        T_STK_INSTOCKENTRY a
                    JOIN T_STK_INSTOCK b ON a.FID = b.FID
                    JOIN T_STK_INSTOCKENTRY_F c ON a.FID = c.FID and a.FENTRYID = c.FENTRYID
                    JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
                    WHERE
                        b.FDATE < :dateEnd
                        and f.FERPCLSID = '1'
                        """)
                        if(supplierIdList.size>0){
                            sb.append(""" and b.FSUPPLIERID in (:supplierIdList)  """)
                        }
                        if(departmentIdList.size>0){
                            sb.append(""" and b.fpurchaseDeptId in (:departmentIdList)  """)
                        }
                //-采购退料单
                sb.append("""
                UNION ALL
                    SELECT
                        b.FSUPPLIERID AS supplierId,
                        b.fmrDeptId AS departmentId,
                        - c.FALLAMOUNT AS fsamount
                    FROM
                        T_PUR_MRBENTRY a
                    JOIN T_PUR_MRB b ON a.FID = b.FID
                    JOIN T_PUR_MRBENTRY_F c ON a.FID = c.FID and a.FENTRYID = c.FENTRYID
                    JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
                    WHERE
                        b.FDATE < :dateEnd
                        and f.FERPCLSID = '1'
                        """)
                        if(supplierIdList.size>0){
                            sb.append(""" and b.FSUPPLIERID in (:supplierIdList)  """)
                        }
                        if(departmentIdList.size>0){
                            sb.append(""" and b.fmrDeptId in (:departmentIdList)  """)
                        }
                //应付单
                sb.append("""
                UNION ALL
                    SELECT
                        b.FSUPPLIERID AS supplierId,
                        b.fpurchaseDeptId AS departmentId,
                        a.FALLAMOUNT AS FAMOUNT
                    FROM
                        T_AP_PAYABLEENTRY a
                        JOIN T_AP_PAYABLE b ON a.FID = b.FID
                        JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
                    WHERE
                         b.FDATE < :dateEnd
                         and f.FERPCLSID = '6'
                        """)
                        if(supplierIdList.size>0){
                            sb.append(""" and b.FSUPPLIERID in (:supplierIdList)  """)
                        }
                        if(departmentIdList.size>0){
                            sb.append(""" and b.fpurchaseDeptId in (:departmentIdList)  """)
                        }
                //其他应付单
                sb.append("""
                UNION ALL
                    SELECT
                        b.FCONTACTUNIT AS supplierId,
                        a.fcostDepartmentId AS departmentId,
                        a.FTOTALAMOUNT
                    FROM
                        T_AP_OTHERPAYABLEENTRY a
                        JOIN T_AP_OTHERPAYABLE b ON a.FID = b.FID
                    WHERE
                        b.FDATE < :dateEnd
                        """)
                        if(supplierIdList.size>0){
                            sb.append(""" and b.FCONTACTUNIT in (:supplierIdList)  """)
                        }
                        if(departmentIdList.size>0){
                            sb.append(""" and a.fcostDepartmentId in (:departmentIdList)  """)
                        }
                //-付款单
                sb.append("""
                UNION ALL
                    SELECT
                        b.FCONTACTUNIT AS supplierId,
                        b.fpurchasedeptid AS departmentId,
                        - a.FPAYTOTALAMOUNT AS FAMOUNT
                    FROM
                        T_AP_PAYBILLENTRY a
                        JOIN T_AP_PAYBILL b ON a.FID = b.FID
                    WHERE
                        b.FDATE < :dateEnd
                        """)
                        if(supplierIdList.size>0){
                            sb.append(""" and b.FCONTACTUNIT in (:supplierIdList)  """)
                        }
                        if(departmentIdList.size>0){
                            sb.append(""" and b.fpurchasedeptid in (:departmentIdList)  """)
                        }
                //付款退款单
                sb.append("""
                UNION ALL
                    SELECT
                        b.FCONTACTUNIT AS supplierId,
                        b.fpurchasedeptid AS departmentId,
                        a.FREFUNDAMOUNT AS FAMOUNT
                    FROM
                        T_AP_REFUNDBILLENTRY a
                        JOIN T_AP_REFUNDBILL b ON a.FID = b.FID
                    WHERE
                        b.FDATE < :dateEnd
                        """)
                        if(supplierIdList.size>0){
                            sb.append(""" and b.FCONTACTUNIT in (:supplierIdList)  """)
                        }
                        if(departmentIdList.size>0){
                            sb.append(""" and b.fpurchasedeptid in (:departmentIdList)  """)
                        }
            sb.append("""
                ) a
                GROUP BY
                    a.supplierId,
                    a.departmentId
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(SupplierPayableDto::class.java))
    }

    //应付金额：sum(采购入库单-采购退料单+其他应付单+应付单)
    fun findShouldPay(supplierPayableQuery: SupplierPayableQuery): MutableList<SupplierPayableDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", supplierPayableQuery.dateStart.toString())
        paramMap.put("dateEnd", supplierPayableQuery.dateEnd.toString())
        paramMap.put("supplierIdList", supplierPayableQuery.supplierIdList)
        paramMap.put("departmentIdList",supplierPayableQuery.departmentIdList)
        var sb = StringBuilder("""
            SELECT
                a.supplierId AS supplierId,
                a.departmentId AS departmentId,
                SUM (a.beginAmount) AS beginAmount
            FROM
            (
            """)
                //采购入库单
                sb.append("""
                SELECT
                    b.FSUPPLIERID AS supplierId,
                    b.fpurchaseDeptId AS departmentId,
                    c.FALLAMOUNT  AS beginAmount
                FROM
                  T_STK_INSTOCKENTRY a
                JOIN T_STK_INSTOCK b ON a.FID = b.FID
                JOIN T_STK_INSTOCKENTRY_F c ON a.FID = c.FID and a.FENTRYID = c.FENTRYID
                JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
                WHERE
                    b.FDATE >= :dateStart
                    and b.FDATE  <= :dateEnd
                    and f.FERPCLSID = '1'
                    """)
                    if(supplierPayableQuery.supplierIdList.size>0) {
                        sb.append("""  and b.FSUPPLIERID in (:supplierIdList)  """)
                    }
                    if (supplierPayableQuery.departmentIdList.size>0){
                        sb.append("""  and b.fpurchaseDeptId in (:departmentIdList)  """)
                    }
                //-采购退料单
                sb.append("""
            union all
                SELECT
                    b.FSUPPLIERID AS supplierId,
                    b.fmrDeptId AS departmentId,
                    -c.FALLAMOUNT AS beginAmount
                FROM
                  T_PUR_MRBENTRY a
                JOIN T_PUR_MRB b ON a.FID = b.FID
                JOIN T_PUR_MRBENTRY_F c ON a.FID = c.FID and a.FENTRYID = c.FENTRYID
                JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
                WHERE
                    b.FDATE >= :dateStart
                    and b.FDATE <= :dateEnd
                    and f.FERPCLSID = '1'
                    """)
                    if(supplierPayableQuery.supplierIdList.size>0) {
                        sb.append("""  and b.FSUPPLIERID in (:supplierIdList)  """)
                    }
                    if (supplierPayableQuery.departmentIdList.size>0){
                        sb.append("""  and b.fmrDeptId in (:departmentIdList)  """)
                    }
                //其他应付单
                sb.append("""
            union all
                SELECT
                    b.FCONTACTUNIT AS supplierId,
                    a.fcostDepartmentId AS departmentId,
                    a.FTOTALAMOUNT AS beginAmount
                FROM
                  T_AP_OTHERPAYABLEENTRY a
                JOIN T_AP_OTHERPAYABLE b ON a.FID = b.FID
                WHERE
                    b.FDATE >= :dateStart
                    and b.FDATE <= :dateEnd
                    """)
                    if(supplierPayableQuery.supplierIdList.size>0) {
                        sb.append("""  and b.FCONTACTUNIT in (:supplierIdList)  """)
                    }
                    if (supplierPayableQuery.departmentIdList.size>0){
                        sb.append("""  and a.fcostDepartmentId in (:departmentIdList)  """)
                    }
                //应付单
                sb.append("""
            union all
                SELECT
                    b.FSUPPLIERID AS supplierId,
                    b.fpurchaseDeptId AS departmentId,
                    a.FALLAMOUNT AS beginAmount
                FROM
                  T_AP_PAYABLEENTRY a
                JOIN T_AP_PAYABLE b ON a.FID = b.FID
                JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
                WHERE
                    b.FDATE >= :dateStart
                    and b.FDATE  <= :dateEnd
                    and f.FERPCLSID = '6'
                    """)
                    if(supplierPayableQuery.supplierIdList.size>0) {
                        sb.append("""  and b.FSUPPLIERID in (:supplierIdList)  """)
                    }
                    if (supplierPayableQuery.departmentIdList.size>0){
                        sb.append("""  and b.fpurchaseDeptId in (:departmentIdList)  """)
                    }
        sb.append("""
                ) a
              GROUP BY
              a.supplierId,
              a.departmentId
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(SupplierPayableDto::class.java))
    }

    //实付金额：sum(付款单-付款退款单)
    fun findActualPay(supplierPayableQuery: SupplierPayableQuery): MutableList<SupplierPayableDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", supplierPayableQuery.dateStart.toString())
        paramMap.put("dateEnd", supplierPayableQuery.dateEnd.toString())
        paramMap.put("supplierIdList", supplierPayableQuery.supplierIdList)
        paramMap.put("departmentIdList",supplierPayableQuery.departmentIdList)
        var sb = StringBuilder("""
            SELECT
                a.supplierId AS supplierId,
                a.departmentId AS departmentId,
                SUM (a.beginAmount) AS beginAmount
            FROM
            (
                """)
        //付款单
        sb.append("""
                SELECT
                    b.FCONTACTUNIT AS supplierId,
                    b.fpurchasedeptid AS departmentId,
                    a.FPAYTOTALAMOUNT AS beginAmount
                FROM
                    T_AP_PAYBILLENTRY a
                JOIN T_AP_PAYBILL b ON a.FID = b.FID
                WHERE
                    b.FDATE >= :dateStart
                    and b.FDATE  <= :dateEnd
                """)
                if(supplierPayableQuery.supplierIdList.size>0) {
                    sb.append(""" and b.FCONTACTUNIT in (:supplierIdList) """)
                }
                if (supplierPayableQuery.departmentIdList.size>0){
                    sb.append(""" and b.fpurchasedeptid in (:departmentIdList) """)
                }
        //-付款退款单
        sb.append("""
            union all
                SELECT
                    b.FCONTACTUNIT AS supplierId,
                    b.fpurchasedeptid AS departmentId,
                    -a.FREFUNDAMOUNT AS beginAmount
                FROM
                  T_AP_REFUNDBILLENTRY a
                JOIN T_AP_REFUNDBILL b ON a.FID = b.FID
                WHERE
                    b.FDATE >= :dateStart
                    and b.FDATE  <= :dateEnd
                   """)
                if(supplierPayableQuery.supplierIdList.size>0) {
                    sb.append(""" and b.FCONTACTUNIT in (:supplierIdList) """)
                }
                if (supplierPayableQuery.departmentIdList.size>0){
                    sb.append(""" and b.fpurchasedeptid in (:departmentIdList) """)
                }
        sb.append("""
                ) a
              GROUP BY
              a.supplierId,
              a.departmentId
        """);
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(SupplierPayableDto::class.java))
    }

    //采购入库单sum+采购退料单sum+应付单sum+付款单+付款退款单+其他应付单
    fun findMainList(supplierPayableQuery: SupplierPayableQuery): MutableList<SupplierPayableDetailDto>?{
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", supplierPayableQuery.dateStart.toString())
        paramMap.put("dateEnd", supplierPayableQuery.dateEnd.toString())
        paramMap.put("supplierIdList", supplierPayableQuery.supplierIdList)
        paramMap.put("departmentIdList",supplierPayableQuery.departmentIdList)
        var sb = StringBuilder("""
            select
                t.supplierId,
                t.departmentId,
                t.date,
                t.billType,
                t.billNo,
                t.materialId,
                t.qty,
                t.amount,
                t.note,
                t.documentStatus
            from (
        """)
        //采购入库单sum
        sb.append("""
            SELECT
                b.FSUPPLIERID AS supplierId,
                b.fpurchaseDeptId AS departmentId,
                b.FDATE  as date,
                d.FNAME AS billType,
                b.FBILLNO  as billNo,
                null AS materialId,
				null AS qty,
                sum(c.FALLAMOUNT)  AS amount,
                null as note,
                b.FDOCUMENTSTATUS as documentStatus
            FROM
              T_STK_INSTOCKENTRY a
            JOIN T_STK_INSTOCK b ON a.FID = b.FID
            JOIN T_STK_INSTOCKENTRY_F c ON a.FID = c.FID and a.FENTRYID = c.FENTRYID
            JOIN T_BAS_BILLTYPE_L d ON b.FBILLTYPEID = d.FBILLTYPEID
            JOIN t_bd_supplier e ON b.FSUPPLIERID = e.FSUPPLIERID
            JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                f.FERPCLSID = '1'
                and b.FDATE >= :dateStart
                and b.FDATE  <= :dateEnd
                """)
                if(supplierPayableQuery.supplierIdList.size>0){
                    sb.append("""  and b.FSUPPLIERID in (:supplierIdList)  """)
                }
                if(supplierPayableQuery.departmentIdList.size>0){
                    sb.append("""  and b.fpurchaseDeptId in (:departmentIdList)  """)
                }
            sb.append("""
            GROUP BY
                b.FSUPPLIERID,
                b.fpurchaseDeptId,
                b.FDATE,
                d.FNAME,
                b.FBILLNO,
                b.FDOCUMENTSTATUS
        """)
        //采购退料单sum
        sb.append("""
        union all
            SELECT
                b.FSUPPLIERID AS supplierId,
                b.fmrDeptId AS departmentId,
                b.FDATE AS DATE,
                d.FNAME AS billType,
                b.FBILLNO AS billNo,
                null AS materialId,
				null AS qty,
                sum(c.FALLAMOUNT) AS amount,
                null as note,
                b.FDOCUMENTSTATUS as documentStatus
            FROM
              T_PUR_MRBENTRY a
            JOIN T_PUR_MRB b ON a.FID = b.FID
            JOIN T_PUR_MRBENTRY_F c ON a.FID = c.FID and a.FENTRYID = c.FENTRYID
            JOIN T_BAS_BILLTYPE_L d ON b.FBILLTYPEID = d.FBILLTYPEID
            JOIN t_bd_supplier e ON b.FSUPPLIERID = e.FSUPPLIERID
            JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                f.FERPCLSID = '1'
                and b.FDATE >= :dateStart
                and b.FDATE <= :dateEnd
            """)
                if(supplierPayableQuery.supplierIdList.size>0){
                    sb.append("""  and b.FSUPPLIERID in (:supplierIdList)  """)
                }
                if(supplierPayableQuery.departmentIdList.size>0){
                    sb.append("""  and b.fmrDeptId in (:departmentIdList)  """)
                }
            sb.append("""
            GROUP BY
                b.FSUPPLIERID,
                b.fmrDeptId,
                b.FDATE,
                d.FNAME,
                b.FBILLNO,
                b.FDOCUMENTSTATUS
        """)
        //应付单sum
        sb.append("""
        union all
            SELECT
                b.FSUPPLIERID AS supplierId,
                b.fpurchaseDeptId AS departmentId,
                b.FDATE as date,
                d.FNAME AS billType,
                b.FBILLNO  as billNo,
                null AS materialId,
				null AS qty,
                sum(a.FALLAMOUNT) AS amount,
                null as note,
                b.FDOCUMENTSTATUS as documentStatus
            FROM
              T_AP_PAYABLEENTRY a
            JOIN T_AP_PAYABLE b ON a.FID = b.FID
            JOIN T_BAS_BILLTYPE_L d ON b.FBILLTYPEID = d.FBILLTYPEID
            JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                f.FERPCLSID = '6'
                and b.FDATE >= :dateStart
                and b.FDATE  <= :dateEnd
                """)
                if(supplierPayableQuery.supplierIdList.size>0){
                    sb.append("""  and b.FSUPPLIERID in (:supplierIdList)  """)
                }
                if(supplierPayableQuery.departmentIdList.size>0){
                    sb.append("""  and b.fpurchaseDeptId in (:departmentIdList)  """)
                }
            sb.append("""
            GROUP BY
                b.FSUPPLIERID,
                b.fpurchaseDeptId,
                b.FDATE,
                d.FNAME,
                b.FBILLNO,
                b.FDOCUMENTSTATUS
            """)
//        //付款单
        sb.append("""
        union all
            SELECT
                b.FCONTACTUNIT AS supplierId,
                b.fpurchasedeptid AS departmentId,
                b.FDATE AS date,
                d.FNAME AS billType,
                b.FBILLNO AS billNo,
                null AS materialId,
				null AS qty,
                a.FPAYTOTALAMOUNT AS amount,
                a.FCOMMENT as note,
                b.FDOCUMENTSTATUS as documentStatus
            FROM
                T_AP_PAYBILLENTRY a
            JOIN T_AP_PAYBILL b ON a.FID = b.FID
            LEFT JOIN T_BAS_BILLTYPE_L d ON b.FBILLTYPEID = d.FBILLTYPEID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE  <= :dateEnd
        """)
                if(supplierPayableQuery.supplierIdList.size>0){
                    sb.append("""  and b.FCONTACTUNIT in (:supplierIdList)  """)
                }
                if(supplierPayableQuery.departmentIdList.size>0){
                    sb.append("""  and b.fpurchasedeptid in (:departmentIdList)  """)
                }
//        //付款退款单
        sb.append("""
        union all
            SELECT
                b.FCONTACTUNIT AS supplierId,
                b.fpurchasedeptid AS departmentId,
                b.FDATE as date,
                d.FNAME AS billType,
                b.FBILLNO as billNo,
                null AS materialId,
				null AS qty,
                a.FREFUNDAMOUNT AS amount,
                a.FNOTE as note,
                b.FDOCUMENTSTATUS as documentStatus
            FROM
                T_AP_REFUNDBILLENTRY a
            JOIN T_AP_REFUNDBILL b ON a.FID = b.FID
            LEFT JOIN T_BAS_BILLTYPE_L d ON b.FBILLTYPEID = d.FBILLTYPEID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE  <= :dateEnd
        """)
                if(supplierPayableQuery.supplierIdList.size>0){
                    sb.append("""  and b.FCONTACTUNIT in (:supplierIdList)  """)
                }
                if(supplierPayableQuery.departmentIdList.size>0){
                    sb.append("""  and b.fpurchasedeptid in (:departmentIdList)  """)
                }
//        //其他应付单
        sb.append("""
        union all
            SELECT
                b.FCONTACTUNIT AS supplierId,
                a.fcostDepartmentId AS departmentId,
                b.FDATE as date,
                d.FNAME AS billType,
                b.FBILLNO as billNo,
                c.FNAME as materialId,
                c.FNUMBER as qty,
                a.FTOTALAMOUNT AS amount,
                a.FCOMMENT as note,
                b.FDOCUMENTSTATUS as documentStatus
            FROM
                T_AP_OTHERPAYABLEENTRY a
            JOIN T_AP_OTHERPAYABLE b ON a.FID = b.FID
            LEFT JOIN T_BAS_BILLTYPE_L d ON b.FBILLTYPEID = d.FBILLTYPEID
            LEFT JOIN (
                SELECT
                    a.FENTRYID,
                    a.FNUMBER,
                    b.FDATAVALUE AS FNAME
                FROM
                    T_BAS_ASSISTANTDATAENTRY a
                JOIN T_BAS_ASSISTANTDATAENTRY_L b ON a.FENTRYID = b.FENTRYID
                JOIN t_bas_assistantdata c ON a.FID = c.FID
                JOIN T_BAS_ASSISTANTDATA_L d ON c.FID = d.FID
                WHERE
                    d.FNAME = '费用类'
            ) c ON a.F_PAEC_ASSISTANT1 = c.FENTRYID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE  <= :dateEnd
        """)
                if(supplierPayableQuery.supplierIdList.size>0){
                    sb.append("""  and b.FCONTACTUNIT in (:supplierIdList)  """)
                }
                if(supplierPayableQuery.departmentIdList.size>0){
                    sb.append("""  and a.fcostDepartmentId in (:departmentIdList)  """)
                }
        sb.append("""
            ) t
            order by
                t.supplierId,
                t.departmentId,
                t.date
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(SupplierPayableDetailDto::class.java))
    }

    //显示物料: 采购入库单+采购退料单+应付单
    fun findDetailList(supplierPayableQuery: SupplierPayableQuery): MutableList<SupplierPayableDetailDto>?{
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", supplierPayableQuery.dateStart.toString())
        paramMap.put("dateEnd", supplierPayableQuery.dateEnd.toString())
        paramMap.put("supplierIdList", supplierPayableQuery.supplierIdList)
        paramMap.put("departmentIdList",supplierPayableQuery.departmentIdList)
        var sb = StringBuilder("")
        //采购入库单
        sb.append("""
            SELECT
                b.FSUPPLIERID AS supplierId,
                b.fpurchaseDeptId AS departmentId,
                b.FDATE  as date,
                d.FNAME AS billType,
                b.FBILLNO  as billNo,
                a.FMATERIALID as materialId,
                a.FBASEUNITQTY AS qty,
                c.FALLAMOUNT  AS amount,
                a.FNOTE as note,
                b.FDOCUMENTSTATUS as documentStatus
            FROM
                T_STK_INSTOCKENTRY a
            JOIN T_STK_INSTOCK b ON a.FID = b.FID
            JOIN T_STK_INSTOCKENTRY_F c ON a.FID = c.FID and a.FENTRYID = c.FENTRYID
            JOIN T_BAS_BILLTYPE_L d ON b.FBILLTYPEID = d.FBILLTYPEID
            JOIN t_bd_supplier e ON b.FSUPPLIERID = e.FSUPPLIERID
            JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE  <= :dateEnd
                and f.FERPCLSID = '1'
                """)
        if(supplierPayableQuery.supplierIdList.size>0){
            sb.append("""  and b.FSUPPLIERID in (:supplierIdList)  """)
        }
        if(supplierPayableQuery.departmentIdList.size>0){
            sb.append("""  and b.fpurchaseDeptId in (:departmentIdList)  """)
        }
        //采购退料单
        sb.append("""
        union all
            SELECT
                b.FSUPPLIERID AS supplierId,
                b.fmrDeptId AS departmentId,
                b.FDATE AS DATE,
                d.FNAME AS billType,
                b.FBILLNO AS billNo,
                a.FMATERIALID AS materialId,
                a.FBASEUNITQTY AS qty,
                c.FALLAMOUNT AS amount,
                a.FNOTE AS note,
                b.FDOCUMENTSTATUS as documentStatus
            FROM
                T_PUR_MRBENTRY a
            JOIN T_PUR_MRB b ON a.FID = b.FID
            JOIN T_PUR_MRBENTRY_F c ON a.FID = c.FID and a.FENTRYID = c.FENTRYID
            JOIN T_BAS_BILLTYPE_L d ON b.FBILLTYPEID = d.FBILLTYPEID
            JOIN t_bd_supplier e ON b.FSUPPLIERID = e.FSUPPLIERID
            JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE <= :dateEnd
                and f.FERPCLSID = '1'
            """)
        if(supplierPayableQuery.supplierIdList.size>0){
            sb.append("""  and b.FSUPPLIERID in (:supplierIdList)  """)
        }
        if(supplierPayableQuery.departmentIdList.size>0){
            sb.append("""  and b.fmrDeptId in (:departmentIdList)  """)
        }
        //应付单
        sb.append("""
        union all
            SELECT
                b.FSUPPLIERID AS supplyId,
                b.fpurchaseDeptId AS departmentId,
                b.FDATE as date,
                d.FNAME AS billType,
                b.FBILLNO  as billNo,
                a.FMATERIALID as materialId,
                null as qty,
                a.FALLAMOUNT  AS amount,
                a.FCOMMENT  as note,
                b.FDOCUMENTSTATUS as documentStatus
            FROM
              T_AP_PAYABLEENTRY a
            JOIN T_AP_PAYABLE b ON a.FID = b.FID
            JOIN T_BAS_BILLTYPE_L d ON b.FBILLTYPEID = d.FBILLTYPEID
            JOIN T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE  <= :dateEnd
                and f.FERPCLSID = '6'
                """)
        if(supplierPayableQuery.supplierIdList.size>0){
            sb.append("""  and b.FSUPPLIERID in (:supplierIdList)  """)
        }
        if(supplierPayableQuery.departmentIdList.size>0){
            sb.append("""  and b.fpurchaseDeptId in (:departmentIdList)  """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(SupplierPayableDetailDto::class.java))
    }

}