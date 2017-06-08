package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.report.dto.RetailAccountDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * 销售退货单
 * Created by lihx on 2017/6/7.
 */
@Component
class SalReturnStockRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findByPeriodForTotalDepartment(year:Integer, month:Integer): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            year(b.FDATE) AS YEAR,
            month(b.FDATE) AS month,
            '销售退货单' AS fyName,
            '02' AS  fyNum,
            '00' as deptNum,
            '所有部门合计' as deptName,
            'Y' as accName,
            isnull(SUM (a.frealqty),0) AS amount
        FROM
            T_SAL_RETURNSTOCKENTRY a
        JOIN T_SAL_RETURNSTOCK b ON a.FID = b.FID
        join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
        join t_bd_materialcategory g on f.FCATEGORYID = g.FCATEGORYID
        WHERE
            year(b.FDATE) = :year
            and MONTH (b.FDATE) = :month
            AND g.FNUMBER IN ('CHLB10')
        GROUP BY
            year(b.FDATE),
            MONTH (b.FDATE)
        """, Collections.singletonMap("year",year),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findBySumPeriodForTotalDepartment(dateStart:Integer, dateEnd:Integer): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            '销售退货单' AS fyName,
            '02' AS fyNum,
            '00' as deptNum,
            '所有部门合计' as deptName,
            'Y' as accName,
            isnull(SUM (a.frealqty),0) AS amount
        FROM
            T_SAL_RETURNSTOCKENTRY a
        JOIN T_SAL_RETURNSTOCK b ON a.FID = b.FID
        join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
        join t_bd_materialcategory g on f.FCATEGORYID = g.FCATEGORYID
        WHERE
          YEAR(b.FDATE)*100+MONTH(b.FDATE) BETWEEN :dateStart AND :dateEnd
          AND g.FNUMBER IN ('CHLB10')
        """,Collections.singletonMap("dateStart",dateStart),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findByPeriod(year:Integer, month:Integer): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            year(b.FDATE) AS YEAR,
            month(b.FDATE) AS month,
            '销售退货单' AS fyName,
            '02' AS  fyNum,
            'Y' as accName,
            c.FNUMBER AS deptNum,
            c.FNAME as deptName,
            isnull(SUM (a.frealqty),0) AS amount
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
        join t_bd_materialcategory g on f.FCATEGORYID = g.FCATEGORYID
        WHERE
            year(b.FDATE) = :year
            and MONTH (b.FDATE) = :month
            and g.FNUMBER IN ('CHLB10')
        GROUP BY
            year(b.FDATE),
            MONTH (b.FDATE),
            c.FNUMBER,
            c.FNAME
        """, Collections.singletonMap("year",year),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findBySumPeriod(dateStart:Integer, dateEnd:Integer): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            '销售退货单' AS fyName,
            '02' AS fyNum,
            'Y' as accName,
            c.FNAME AS deptName,
            c.FNUMBER AS deptNum,
            isnull(SUM (a.frealqty),0) AS amount
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
        join t_bd_materialcategory g on f.FCATEGORYID = g.FCATEGORYID
        WHERE
          YEAR(b.FDATE)*100+MONTH(b.FDATE) BETWEEN :dateStart AND :dateEnd
          AND g.FNUMBER IN ('CHLB10')
        group by
          c.FNAME,
	      c.FNUMBER
        """,Collections.singletonMap("dateStart",dateStart),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }
}