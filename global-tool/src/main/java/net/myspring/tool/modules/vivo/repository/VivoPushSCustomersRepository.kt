package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPushSCustomers
import net.myspring.tool.modules.vivo.dto.FutureCustomerDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate

interface VivoPushSCustomersRepository :BaseRepository<VivoPushSCustomers,String>,VivoPushSCustomersRepositoryCustom{

}

interface VivoPushSCustomersRepositoryCustom{
    fun findFutureVivoCustomers(date: LocalDate):MutableList<FutureCustomerDto>
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
}