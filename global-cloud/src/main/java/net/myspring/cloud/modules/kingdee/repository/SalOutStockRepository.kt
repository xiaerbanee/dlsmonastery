package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.report.dto.RetailAccountDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * 销售出库单
 * Created by lihx on 2017/6/7.
 */
@Component
class SalOutStockRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findByPeriodForTotalDepartment(year:Int, month:Int): MutableList<RetailAccountDto>?{

        return namedParameterJdbcTemplate.query("""
        SELECT
            year(b.FDATE) AS YEAR,
            month(b.FDATE) AS month,
            '销售出库单' AS fyName,
            '01' AS fyNum,
            '00' as deptNum,
            '所有部门合计' as deptName,
            'Y' as accName,
            isnull(sum(a.frealqty),0) AS amount
        FROM
            T_SAL_OUTSTOCKENTRY a
        JOIN T_SAL_OUTSTOCK b ON a.FID = b.FID
        join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
        join t_bd_materialcategory g on f.FCATEGORYID = g.FCATEGORYID
        WHERE
            year(b.FDATE) = :year
            and MONTH (b.FDATE) = :month
            and g.FNUMBER IN ('CHLB10')
        GROUP BY
            year(b.FDATE),
            MONTH (b.FDATE)
        """,Collections.singletonMap("year",year),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findBySumPeriodForTotalDepartment(dateStart:Int, dateEnd:Int): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            '销售出库单' AS fyName,
            '01' AS fyNum,
            'Y' as accName,
            '00' as deptNum,
            '所有部门合计' as deptName,
            isnull(SUM (a.frealqty),0) AS amount
        FROM
            T_SAL_OUTSTOCKENTRY a
        JOIN T_SAL_OUTSTOCK b ON a.FID = b.FID
        join T_BD_MATERIALBASE f ON a.FMATERIALID = f.FMATERIALID
        join t_bd_materialcategory g on f.FCATEGORYID = g.FCATEGORYID
        WHERE
            year(b.FDATE)*100+month(b.FDATE) BETWEEN :dateStart AND :dateEnd
            AND g.FNUMBER IN ('CHLB10')
        """,Collections.singletonMap("dateStart",dateStart),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findByPeriod(year:Int, month:Int): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            year(b.FDATE) AS YEAR,
            month(b.FDATE) AS month,
            '销售出库单' AS fyName,
            '01' AS fyNum,
            'Y' as accName,
            c.FNAME as deptName,
            c.FNUMBER AS deptNum,
            isnull(sum(a.frealqty),0) AS amount
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

    fun findBySumPeriod(dateStart:Int, dateEnd:Int): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            '销售出库单' AS fyName,
            '01' AS fyNum,
            'Y' as accName,
            c.FNAME AS deptName,
	        c.FNUMBER AS deptNum,
            isnull(SUM (a.frealqty),0) AS amount
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
        join t_bd_materialcategory g on f.FCATEGORYID = g.FCATEGORYID
        WHERE
          year(b.FDATE)*100+month(b.FDATE) BETWEEN :dateStart AND :dateEnd
          AND g.FNUMBER IN ('CHLB10')
       group by
          c.FNAME,
	      c.FNUMBER
        """,Collections.singletonMap("dateStart",dateStart),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }
}
