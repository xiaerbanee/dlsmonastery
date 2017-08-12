package net.myspring.cloud.modules.report.repository

import net.myspring.cloud.modules.report.dto.CustomerInventoryDto
import net.myspring.util.time.LocalDateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.HashMap

/**
 * 温州-委托代销报表
 */
@Component
class ConsignmentWZRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findByEndDate(dateEnd: LocalDate): MutableList<CustomerInventoryDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateEnd",LocalDateUtils.format(dateEnd))
        var sb = StringBuilder();
        sb.append("""
            SELECT
                temp.customerId,
                temp.customerName,
                temp.materialId,
                temp.materialName,
                sum(temp.quantity) as quantity,
                temp.price,
                sum(temp.amount) as amount
            FROM (
        """)
        //初始化数据Initialization
        sb.append("""
            SELECT
                e.FCUSTID as customerId,
                e.FNAME as customerName,
                a.FMATERIALID as materialId,
                f.FNAME as materialName,
                isnull(a.FQTY,0) AS quantity,
                a.F_PAEC_PRICE AS price,
                isnull(a.F_PAEC_AMOUNT,0) AS amount
            FROM
                T_STK_INVINITDETAIL a
            JOIN t_BD_Stock d ON a.FSTOCKID = d.FSTOCKID
            JOIN T_BD_CUSTOMER_L e ON d.FCUSTOMERID = e.FCUSTID
            JOIN T_BD_MATERIAL_L f ON a.FMATERIALID = f.FMATERIALID
        """)
        sb.append(""" union all """)
        //findTransferInByEndDate
        sb.append("""
            SELECT
                e.FCUSTID as customerId,
                e.FNAME as customerName,
                a.FMATERIALID as materialId,
                f.FNAME as materialName,
                a.FQTY AS quantity,
                c.FTAXPRICE as price,
                c.FALLAMOUNT AS amount
            FROM
                T_STK_STKTRANSFERINENTRY a
                JOIN T_STK_STKTRANSFERIN b ON a.FID = b.FID
                JOIN T_STK_STKTRANSFERINENTRY_T c ON a.fid = c.fid AND a.FENTRYID = c.FENTRYID
                JOIN t_BD_Stock d ON a.FDESTSTOCKID = d.FSTOCKID
                JOIN T_BD_CUSTOMER_L e ON d.FCUSTOMERID = e.FCUSTID
                JOIN T_BD_MATERIAL_L f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE < :dateEnd
        """)
        sb.append(""" union all """)
        //findTransferOutByEndDate
        sb.append("""
            SELECT
                e.FCUSTID as customerId,
                e.FNAME as customerName,
                a.FMATERIALID as materialId,
                f.FNAME as materialName,
                a.FQTY AS quantity,
                c.FTAXPRICE as price,
                c.FALLAMOUNT AS amount
            FROM
                T_STK_STKTRANSFERINENTRY a
                JOIN T_STK_STKTRANSFERIN b ON a.FID = b.FID
                JOIN T_STK_STKTRANSFERINENTRY_T c ON a.fid = c.fid AND a.FENTRYID = c.FENTRYID
                JOIN t_BD_Stock d ON a.FSRCSTOCKID = d.FSTOCKID
                JOIN T_BD_CUSTOMER_L e ON d.FCUSTOMERID = e.FCUSTID
                JOIN T_BD_MATERIAL_L f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE < :dateEnd
        """)
        sb.append(""" union all """)
        //findOutStockByEndDate
        sb.append("""
            SELECT
                b.FCUSTOMERID AS customerId,
                e.FNAME as customerName,
                a.FMATERIALID as materialId,
                f.FNAME as materialName,
                a.FREALQTY AS quantity,
                c.FTAXPRICE AS price,
                c.FALLAMOUNT AS amount
            FROM
                T_SAL_OUTSTOCKENTRY a
                JOIN T_SAL_OUTSTOCK b ON a.FID = b.FID
                JOIN T_SAL_OUTSTOCKENTRY_F c ON a.FID = c.FID AND a.FENTRYID = c.FENTRYID
                JOIN t_BD_Stock d ON a.FSTOCKID = d.FSTOCKID
                JOIN T_BD_CUSTOMER_L e ON d.FCUSTOMERID = e.FCUSTID
                JOIN T_BD_MATERIAL_L f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE < :dateEnd
        """)
        sb.append(""" union all """)
        //findReturnStockByEndDate
        sb.append("""
            SELECT
                b.FRETCUSTID AS customerId,
                e.FNAME as customerName,
                a.FMATERIALID as materialId,
                f.FNAME as materialName,
                a.FREALQTY AS quantity,
                c.FTAXPRICE AS price,
                c.FALLAMOUNT AS amount
            FROM
                T_SAL_RETURNSTOCKENTRY a
                JOIN T_SAL_RETURNSTOCK b ON a.FID = b.FID
                JOIN T_SAL_RETURNSTOCKENTRY_F c ON a.FID = c.FID AND a.FENTRYID = c.FENTRYID
                JOIN t_BD_Stock d ON a.FSTOCKID = d.FSTOCKID
                JOIN T_BD_CUSTOMER_L e ON d.FCUSTOMERID = e.FCUSTID
                JOIN T_BD_MATERIAL_L f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE < :dateEnd
        """)
        sb.append("""
            ) temp
        GROUP BY
            temp.customerId,
            temp.customerName,
            temp.materialId,
            temp.materialName,
            temp.price
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(CustomerInventoryDto::class.java))
    }

    fun findTransferInByPeriod(dateStart:LocalDate,dateEnd: LocalDate): MutableList<CustomerInventoryDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart",LocalDateUtils.format(dateStart))
        paramMap.put("dateEnd",LocalDateUtils.format(dateEnd))
        var sb = StringBuilder()
        sb.append("""
            SELECT
                e.FCUSTID as customerId,
                e.FNAME as customerName,
                a.FMATERIALID as materialId,
                f.FNAME as materialName,
                a.FQTY AS quantity,
                c.FTAXPRICE as price,
                c.FALLAMOUNT AS amount
            FROM
                T_STK_STKTRANSFERINENTRY a
            JOIN T_STK_STKTRANSFERIN b ON a.FID = b.FID
            JOIN T_STK_STKTRANSFERINENTRY_T c ON a.fid = c.fid AND a.FENTRYID = c.FENTRYID
            JOIN t_BD_Stock d ON a.FDESTSTOCKID = d.FSTOCKID
            JOIN T_BD_CUSTOMER_L e ON d.FCUSTOMERID = e.FCUSTID
            JOIN T_BD_MATERIAL_L f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE <= :dateEnd
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(CustomerInventoryDto::class.java))
    }

    fun findTransferOutByPeriod(dateStart:LocalDate,dateEnd: LocalDate): MutableList<CustomerInventoryDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart",LocalDateUtils.format(dateStart))
        paramMap.put("dateEnd",LocalDateUtils.format(dateEnd))
        var sb = StringBuilder()
        sb.append("""
            SELECT
                e.FCUSTID as customerId,
                e.FNAME as customerName,
                a.FMATERIALID as materialId,
                f.FNAME as materialName,
                a.FQTY AS quantity,
                c.FTAXPRICE as price,
                c.FALLAMOUNT AS amount
            FROM
                T_STK_STKTRANSFERINENTRY a
            JOIN T_STK_STKTRANSFERIN b ON a.FID = b.FID
            JOIN T_STK_STKTRANSFERINENTRY_T c ON a.fid = c.fid AND a.FENTRYID = c.FENTRYID
            JOIN t_BD_Stock d ON a.FSRCSTOCKID = d.FSTOCKID
            JOIN T_BD_CUSTOMER_L e ON d.FCUSTOMERID = e.FCUSTID
            JOIN T_BD_MATERIAL_L f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE <= :dateEnd
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(CustomerInventoryDto::class.java))
    }

    fun findOutStockByPeriod(dateStart:LocalDate,dateEnd: LocalDate): MutableList<CustomerInventoryDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart",LocalDateUtils.format(dateStart))
        paramMap.put("dateEnd",LocalDateUtils.format(dateEnd))
        var sb = StringBuilder()
        sb.append("""
            SELECT
                b.FCUSTOMERID AS customerId,
                e.FNAME as customerName,
                a.FMATERIALID as materialId,
                f.FNAME as materialName,
                a.FREALQTY AS quantity,
                c.FTAXPRICE AS price,
                c.FALLAMOUNT AS amount
            FROM
                T_SAL_OUTSTOCKENTRY a
            JOIN T_SAL_OUTSTOCK b ON a.FID = b.FID
            JOIN T_SAL_OUTSTOCKENTRY_F c ON a.FID = c.FID
            AND a.FENTRYID = c.FENTRYID
            JOIN t_BD_Stock d ON a.FSTOCKID = d.FSTOCKID
            JOIN T_BD_CUSTOMER_L e ON d.FCUSTOMERID = e.FCUSTID
            JOIN T_BD_MATERIAL_L f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE <= :dateEnd
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(CustomerInventoryDto::class.java))
    }

    fun findReturnStockByPeriod(dateStart:LocalDate,dateEnd: LocalDate): MutableList<CustomerInventoryDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart",LocalDateUtils.format(dateStart))
        paramMap.put("dateEnd",LocalDateUtils.format(dateEnd))
        var sb = StringBuilder()
        sb.append("""
            SELECT
                b.FRETCUSTID AS customerId,
                e.FNAME as customerName,
                a.FMATERIALID as materialId,
                f.FNAME as materialName,
                a.FREALQTY AS quantity,
                c.FTAXPRICE AS price,
                c.FALLAMOUNT AS amount
            FROM
                T_SAL_RETURNSTOCKENTRY a
            JOIN T_SAL_RETURNSTOCK b ON a.FID = b.FID
            JOIN T_SAL_RETURNSTOCKENTRY_F c ON a.FID = c.FID AND a.FENTRYID = c.FENTRYID
            JOIN t_BD_Stock d ON a.FSTOCKID = d.FSTOCKID
            JOIN T_BD_CUSTOMER_L e ON d.FCUSTOMERID = e.FCUSTID
            JOIN T_BD_MATERIAL_L f ON a.FMATERIALID = f.FMATERIALID
            WHERE
                b.FDATE >= :dateStart
                and b.FDATE <= :dateEnd
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(CustomerInventoryDto::class.java))
    }


}