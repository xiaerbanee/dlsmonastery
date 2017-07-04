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

    fun findEntityByPeriodForTotalDepartment(year:Int, month:Int, accNameList:List<String>, fyNumList:List<String>): MutableList<RetailAccountDto>?{
        var paramMap = HashMap<String, Any>()
        paramMap.put("year", year)
        paramMap.put("month", month)
        paramMap.put("accNameList", accNameList)
        paramMap.put("fyNumList", fyNumList)
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
        """, paramMap,BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityByPeriodForTotalDepartment(year:Int, month:Int, accName:String): MutableList<RetailAccountDto>?{
        var paramMap = HashMap<String, Any>()
        paramMap.put("year", year)
        paramMap.put("month", month)
        paramMap.put("accName", accName)
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
        """, paramMap,BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityBySumPeriodForTotalDepartment(dateStart:Int, dateEnd:Int, accNameList:List<String>, fyNumList:List<String>): MutableList<RetailAccountDto>?{
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", dateStart)
        paramMap.put("dateEnd", dateEnd)
        paramMap.put("accNameList", accNameList)
        paramMap.put("fyNumList", fyNumList)
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
        """,paramMap,BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityBySumPeriodForTotalDepartment(dateStart:Int, dateEnd:Int, accName:String): MutableList<RetailAccountDto>?{
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", dateStart)
        paramMap.put("dateEnd", dateEnd)
        paramMap.put("accName", accName)
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
        """, paramMap,BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityByPeriod(year:Int, month:Int, accNameList:List<String>, fyNumList:List<String>): MutableList<RetailAccountDto>?{
        var paramMap = HashMap<String, Any>()
        paramMap.put("year", year)
        paramMap.put("month", month)
        paramMap.put("accNameList", accNameList)
        paramMap.put("fyNumList", fyNumList)
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
        """,paramMap,BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityByPeriod(year:Int, month:Int, accName:String): MutableList<RetailAccountDto>?{
        var paramMap = HashMap<String, Any>()
        paramMap.put("year", year)
        paramMap.put("month", month)
        paramMap.put("accName", accName)
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
        """,paramMap,BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityBySumPeriod(dateStart:Int, dateEnd:Int, accNameList:List<String>, fyNumList:List<String>): MutableList<RetailAccountDto>?{
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", dateStart)
        paramMap.put("dateEnd", dateEnd)
        paramMap.put("accNameList", accNameList)
        paramMap.put("fyNumList", fyNumList)
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
        """,paramMap,BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

    fun findEntityBySumPeriod(dateStart:Int, dateEnd:Int, accName:String): MutableList<RetailAccountDto>?{
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", dateStart)
        paramMap.put("dateEnd", dateEnd)
        paramMap.put("accName", accName)
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
        """,paramMap,BeanPropertyRowMapper(RetailAccountDto::class.java))
    }

}