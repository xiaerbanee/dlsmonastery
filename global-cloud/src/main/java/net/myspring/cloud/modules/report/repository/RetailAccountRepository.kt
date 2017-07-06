package net.myspring.cloud.modules.report.repository

import net.myspring.cloud.modules.report.dto.RetailAccountDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

/**
 * 零售费用报表
 * Created by lihx on 2017/7/5.
 */
@Component
class RetailAccountRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    
    fun findSalesMobileQtyBySumPeriod(startYearMonth:Int, endYearMonth:Int): MutableList<RetailAccountDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("startYearMonth", startYearMonth)
        paramMap.put("endYearMonth", endYearMonth)
        var sb = StringBuilder("""
            SELECT
                '销售台数' AS fyName,
                'Y01' AS fyNum,
                'Y' as accName,
                t.FNAME AS deptName,
                t.FNUMBER AS deptNum,
                SUM (t.frealqty) AS amount
            FROM
            (
            """)
        //销售出库单
        sb.append("""
            SELECT
                c.FNAME,
                c.FNUMBER,
                isnull(a.frealqty,0) AS frealqty
            FROM
                T_SAL_OUTSTOCKENTRY a
            JOIN T_SAL_OUTSTOCK b ON a.FID = b.FID
            LEFT JOIN (
                SELECT
                    a.FDEPTID AS FFLEX5,
                    b.FNAME,
                    a.FNUMBER
                FROM
                    t_bd_department a
                JOIN T_BD_DEPARTMENT_L b ON a.FDEPTID = b.FDEPTID
            ) c ON b.FDELIVERYDEPTID = c.FFLEX5
            join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            join t_bd_materialCategory g on f.FCATEGORYID = g.FCATEGORYID
            WHERE
                year(b.FDATE)*100+month(b.FDATE) BETWEEN :startYearMonth and :endYearMonth
                and g.FNUMBER IN ('CHLB10')
        """)
        //-销售退货单
        sb.append("""
        union all
            SELECT
                c.FNAME,
                c.FNUMBER,
                isnull(-a.frealqty,0) AS frealqty
            FROM
                T_SAL_RETURNSTOCKENTRY a
            JOIN T_SAL_RETURNSTOCK b ON a.FID = b.FID
            LEFT JOIN (
                SELECT
                    a.FDEPTID AS FFLEX5,
                    b.FNAME,
                    a.FNUMBER
                FROM
                    t_bd_department a
                JOIN T_BD_DEPARTMENT_L b ON a.FDEPTID = b.FDEPTID
            ) c ON b.FSALEDEPTID = c.FFLEX5
            join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            join t_bd_materialCategory g on f.FCATEGORYID = g.FCATEGORYID
            WHERE
                YEAR(b.FDATE)*100+MONTH(b.FDATE) BETWEEN :startYearMonth and :endYearMonth
                and g.FNUMBER IN ('CHLB10')
            """)
        sb.append("""
            ) t
             GROUP BY
                 t.FNAME,
                 t.FNUMBER      
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findSalesMobileQtyByPeriod(year:Int, month:Int): MutableList<RetailAccountDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("year", year)
        paramMap.put("month", month)
        var sb = StringBuilder("""
            SELECT
                '销售台数' AS fyName,
                'Y01' AS fyNum,
                'Y' as accName,
                t.YEAR,
                t.month,
                t.FNAME AS deptName,
                t.FNUMBER AS deptNum,
                SUM (t.frealqty) AS amount
            FROM
            (
            """)
        //销售出库单
        sb.append("""
            SELECT
                year(b.FDATE) AS YEAR,
                month(b.FDATE) AS month,
                c.FNAME,
                c.FNUMBER,
                isnull(a.frealqty,0) AS frealqty
            FROM
                T_SAL_OUTSTOCKENTRY a
            JOIN T_SAL_OUTSTOCK b ON a.FID = b.FID
            LEFT JOIN (
                SELECT
                    a.FDEPTID AS FFLEX5,
                    b.FNAME,
                    a.FNUMBER
                FROM
                    t_bd_department a
                JOIN T_BD_DEPARTMENT_L b ON a.FDEPTID = b.FDEPTID
            ) c ON b.FDELIVERYDEPTID = c.FFLEX5
            join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            join t_bd_materialCategory g on f.FCATEGORYID = g.FCATEGORYID
            WHERE
                year(b.FDATE) = :year
                and MONTH (b.FDATE) = :month
                and g.FNUMBER IN ('CHLB10')
        """)
        //-销售退货单
        sb.append("""
        union all
            SELECT
                year(b.FDATE) AS YEAR,
                month(b.FDATE) AS month,
                c.FNAME,
                c.FNUMBER,
                isnull(-a.frealqty,0) AS frealqty
            FROM
                T_SAL_RETURNSTOCKENTRY a
            JOIN T_SAL_RETURNSTOCK b ON a.FID = b.FID
            LEFT JOIN (
                SELECT
                    a.FDEPTID AS FFLEX5,
                    b.FNAME,
                    a.FNUMBER
                FROM
                    t_bd_department a
                JOIN T_BD_DEPARTMENT_L b ON a.FDEPTID = b.FDEPTID
            ) c ON b.FSALEDEPTID = c.FFLEX5
            join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            join t_bd_materialCategory g on f.FCATEGORYID = g.FCATEGORYID
            WHERE
                year(b.FDATE) = :year
                and MONTH (b.FDATE) = :month
                and g.FNUMBER IN ('CHLB10')
            """)
        sb.append("""
            ) t
            GROUP BY
                year(b.FDATE),
                MONTH (b.FDATE),
                t.FNAME,
                t.FNUMBER
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findSalesMobileQtyBySumPeriodForTotalDepartment(startYearMonth:Int, endYearMonth:Int): MutableList<RetailAccountDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("startYearMonth", startYearMonth)
        paramMap.put("endYearMonth", endYearMonth)
        var sb = StringBuilder("""
            SELECT
                '销售台数' AS fyName,
                'Y01' AS fyNum,
                'Y' as accName,
                '所有部门合计' AS deptName,
                '00' AS deptNum,
                SUM (t.frealqty) AS amount
            FROM
            (
            """)
        //销售出库单
        sb.append("""
            SELECT
                isnull(a.frealqty,0) AS frealqty
            FROM
                T_SAL_OUTSTOCKENTRY a
            JOIN T_SAL_OUTSTOCK b ON a.FID = b.FID
            join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            join t_bd_materialCategory g on f.FCATEGORYID = g.FCATEGORYID
            WHERE
                year(b.FDATE)*100+month(b.FDATE) BETWEEN :startYearMonth and :endYearMonth
                and g.FNUMBER IN ('CHLB10')
        """)
        //-销售退货单
        sb.append("""
        union all
            SELECT
                isnull(-a.frealqty,0) AS frealqty
            FROM
                T_SAL_RETURNSTOCKENTRY a
            JOIN T_SAL_RETURNSTOCK b ON a.FID = b.FID
            join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            join t_bd_materialCategory g on f.FCATEGORYID = g.FCATEGORYID
            WHERE
                YEAR(b.FDATE)*100+MONTH(b.FDATE) BETWEEN :startYearMonth and :endYearMonth
                and g.FNUMBER IN ('CHLB10')
            """)
        sb.append("""
            ) t
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findSalesMobileQtyByPeriodForTotalDepartment(year:Int, month:Int): MutableList<RetailAccountDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("year", year)
        paramMap.put("month", month)
        var sb = StringBuilder("""
            SELECT
                '销售台数' AS fyName,
                'Y01' AS fyNum,
                'Y' as accName,
                t.YEAR,
                t.month,
                '所有部门合计' AS deptName,
                '00' AS deptNum,
                SUM (t.frealqty) AS amount
            FROM
            (
            """)
        //销售出库单
        sb.append("""
            SELECT
                year(b.FDATE) AS YEAR,
                month(b.FDATE) AS month,
                isnull(a.frealqty,0) AS frealqty
            FROM
                T_SAL_OUTSTOCKENTRY a
            JOIN T_SAL_OUTSTOCK b ON a.FID = b.FID
            join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            join t_bd_materialCategory g on f.FCATEGORYID = g.FCATEGORYID
            WHERE
                year(b.FDATE) = :year
                and MONTH (b.FDATE) = :month
                and g.FNUMBER IN ('CHLB10')
        """)
        //-销售退货单
        sb.append("""
        union all
            SELECT
                year(b.FDATE) AS YEAR,
                month(b.FDATE) AS month,
                isnull(-a.frealqty,0) AS frealqty
            FROM
                T_SAL_RETURNSTOCKENTRY a
            JOIN T_SAL_RETURNSTOCK b ON a.FID = b.FID
            join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
            join t_bd_materialCategory g on f.FCATEGORYID = g.FCATEGORYID
            WHERE
                year(b.FDATE) = :year
                and MONTH (b.FDATE) = :month
                and g.FNUMBER IN ('CHLB10')
            """)
        sb.append("""
            ) t
            GROUP BY
                year(b.FDATE),
                MONTH (b.FDATE)
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

}