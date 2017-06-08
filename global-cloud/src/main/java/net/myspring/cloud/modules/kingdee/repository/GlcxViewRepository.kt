package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.report.dto.RetailAccountDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Glcx视图
 * Created by lihx on 2017/6/7.
 */
@Component
class GlcxViewRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findEntityByPeriodForTotalDepartment(year:Integer, month:Integer, accNameList:List<String>, fyNumList:List<String>): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
            SELECT
            FYEAR as year,
            FPERIOD as month,
            faccnumber as accNumber,
            faccname as accName,
            '00' as deptNum,
            '所有部门合计' as deptName,
            Ffynum as fyNum,
            Ffyname as fyName,
            isnull(SUM (famount),0) AS amount
        FROM
            glcx
        WHERE
            flx = ''
            AND Fyear = :year
            and Fperiod = :month
            AND faccname in (:accNameList)
            and Ffynum in (:fyNumList)
        GROUP BY
            FYEAR,
            FPERIOD,
            faccnumber,
            faccname,
            Ffynum,
            Ffyname
        """, Collections.singletonMap("year",year),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityByPeriodForTotalDepartment(year:Integer, month:Integer, accName:String): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
            SELECT
            FYEAR as year,
            FPERIOD as month,
            faccnumber as accNumber,
            faccname as accName,
            '00' as deptNum,
            '所有部门合计' as deptName,
            Ffynum as fyNum,
            Ffyname as fyName,
            isnull(SUM (famount),0) AS amount
        FROM
            glcx
        WHERE
            flx = ''
            AND Fyear = :year
            and Fperiod = :month
            AND faccname = :accName
        GROUP BY
            FYEAR,
            FPERIOD,
            faccnumber,
            faccname,
            Ffynum,
            Ffyname
        """, Collections.singletonMap("year",year),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityBySumPeriodForTotalDepartment(dateStart:Integer, dateEnd:Integer, accNameList:List<String>, fyNumList:List<String>): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            faccnumber as accNumber,
            faccname as accName,
            Ffynum as fyNum,
            Ffyname as fyName,
            '00' as deptNum,
            '所有部门合计' as deptName,
            isnull(SUM (famount),0) AS amount
        FROM
            glcx
        WHERE
            flx = ''
            AND FYEAR*100+FPERIOD BETWEEN  :dateStart AND :dateEnd
            AND faccname in (:accNameList)
            AND Ffynum in (:fyNumList)
        GROUP BY
            faccnumber,
            faccname,
            Ffynum,
            Ffyname
        """, Collections.singletonMap("dateStart",dateStart),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityBySumPeriodForTotalDepartment(dateStart:Integer, dateEnd:Integer, accName:String): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            faccnumber as accNumber,
            faccname as accName,
            Ffynum as fyNum,
            Ffyname as fyName,
            '00' as deptNum,
            '所有部门合计' as deptName,
            isnull(SUM (famount),0) AS amount
        FROM
            glcx
        WHERE
            flx = ''
            AND FYEAR*100+FPERIOD BETWEEN  :dateStart AND :dateEnd
            AND faccname = :accNameList
        GROUP BY
            faccnumber,
            faccname,
            Ffynum,
            Ffyname
        """, Collections.singletonMap("dateStart",dateStart),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityByPeriod(year:Integer, month:Integer, accNameList:List<String>, fyNumList:List<String>): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
            SELECT
            FYEAR as year,
            FPERIOD as month,
            faccnumber as accNumber,
            faccname as accName,
            Fdeptnum as deptNum,
            Fdeptname as deptName,
            Ffynum as fyNum,
            Ffyname as fyName,
            SUM (famount) AS amount
        FROM
          glcx
        WHERE
            flx = ''
            AND Fyear = :year and Fperiod = :month
            AND faccname in (:accNameList)
            and Ffynum in (:fyNumList)
        GROUP BY
            FYEAR,
            FPERIOD,
            faccnumber,
            faccname,
            Fdeptnum,
            Fdeptname,
            Ffynum,
            Ffyname
        """,Collections.singletonMap("year",year),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityByPeriod(year:Integer, month:Integer, accName:String): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
            SELECT
            FYEAR as year,
            FPERIOD as month,
            faccnumber as accNumber,
            faccname as accName,
            Fdeptnum as deptNum,
            Fdeptname as deptName,
            Ffynum as fyNum,
            Ffyname as fyName,
            SUM (famount) AS amount
        FROM
          glcx
        WHERE
            flx = ''
            AND Fyear = :year and Fperiod = :month
            AND faccname = :accName
        GROUP BY
            FYEAR,
            FPERIOD,
            faccnumber,
            faccname,
            Fdeptnum,
            Fdeptname,
            Ffynum,
            Ffyname
        """,Collections.singletonMap("year",year),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityBySumPeriod(dateStart:Integer, dateEnd:Integer, accNameList:List<String>, fyNumList:List<String>): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            faccnumber as accNumber,
            faccname as accName,
            Fdeptnum as deptNum,
            Fdeptname as deptName,
            Ffynum as fyNum,
            Ffyname as fyName,
            SUM (famount) AS amount
        FROM
            glcx
        WHERE
            flx = ''
            AND FYEAR*100+FPERIOD BETWEEN  :dateStart AND :dateEnd
            AND faccname in (:accName)
            and Ffynum in (:fyNum)
        GROUP BY
            faccnumber,
            faccname,
            Fdeptnum,
            Fdeptname,
            Ffynum,
            Ffynamee
        """,Collections.singletonMap("dateStart",dateStart),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityBySumPeriod(dateStart:Integer, dateEnd:Integer, accName:String): MutableList<RetailAccountDto>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            faccnumber as accNumber,
            faccname as accName,
            Fdeptnum as deptNum,
            Fdeptname as deptName,
            Ffynum as fyNum,
            Ffyname as fyName,
            SUM (famount) AS amount
        FROM
            glcx
        WHERE
            flx = ''
            AND FYEAR*100+FPERIOD BETWEEN  :dateStart AND :dateEnd
            AND faccname = :accName
        GROUP BY
            faccnumber,
            faccname,
            Fdeptnum,
            Fdeptname,
            Ffynum,
            Ffynamee
        """,Collections.singletonMap("dateStart",dateStart),BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

}