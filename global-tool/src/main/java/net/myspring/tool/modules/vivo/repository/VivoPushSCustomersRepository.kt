package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPushSCustomers
import net.myspring.tool.modules.vivo.dto.FutureCustomerDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import java.time.LocalDate

interface VivoPushSCustomersRepository :BaseRepository<VivoPushSCustomers,String>,VivoPushSCustomersRepositoryCustom{

}

interface VivoPushSCustomersRepositoryCustom{
    fun findFutureVivoCustomers(date: LocalDate):MutableList<FutureCustomerDto>
    fun findByDate(dateStart:LocalDate,dateEnd:LocalDate):MutableList<VivoPushSCustomers>
    fun batchSave(vivoPushSCustomersList: MutableList<VivoPushSCustomers>):IntArray
}

class VivoPushSCustomersRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :VivoPushSCustomersRepositoryCustom{
    override fun findFutureVivoCustomers(date: LocalDate): MutableList<FutureCustomerDto> {
        val map = Maps.newHashMap<String, Any>()
        map.put("date",date)
        return namedParameterJdbcTemplate.query("""
        SELECT
            de.area_id AS customerID,
            de. NAME AS customerName,
            de.office_id AS zoneID,
            :date AS recordDate,
            1 AS customerLevel,
            '' AS customerstr1,
            de.area_id AS customerstr4
        FROM
            crm_depot de
        WHERE
            de.area_id IS NOT NULL
        OR de.area_id <> ''
        GROUP BY
            de.area_id
        UNION
            SELECT
                de.id AS CustomerID,
                de. NAME AS CustomerName,
                de.area_id AS ZoneID,
                :date AS recordDate,
                2 AS CustomerLevel,
                de.area_type AS Customerstr1,
                de.area_id AS Customerstr4
            FROM
                crm_depot de
            WHERE
                de.id IN (
                    SELECT
                        depot_id
                    FROM
                        crm_depot_shop
                    WHERE
                        depot_id NOT IN (
                            SELECT
                                depot_id
                            FROM
                                crm_depot_store
                        )
                )
        """,map,BeanPropertyRowMapper(FutureCustomerDto::class.java))
    }

    override fun findByDate(dateStart: LocalDate, dateEnd: LocalDate): MutableList<VivoPushSCustomers> {
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        return namedParameterJdbcTemplate.query("""
            SELECT *
            FROM vivo_push_scustomers
            WHERE created_date >= :dateStart
                AND created_date < :dateEnd
        """,map,BeanPropertyRowMapper(VivoPushSCustomers::class.java))
    }

    override fun batchSave(vivoPushSCustomersList: MutableList<VivoPushSCustomers>): IntArray {
        val sb = StringBuffer()
        sb.append("""
           insert into vivo_push_scustomers
            (
                customer_id,
                customer_name,
                zone_id,
                company_id,
                record_date,
                customer_level,
                customerStr1,
                customerStr4,
                customerStr10,
                created_date
            )
            values(
                :customerId,
                :customerName,
                :zoneId,
                :companyId,
                :recordDate,
                :customerLevel,
                :customerstr1,
                :customerstr4,
                :customerstr10,
                :createdDate
            )""")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(vivoPushSCustomersList.toTypedArray()))
    }
}