package net.myspring.tool.modules.future.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.future.dto.CustomerDto
import net.myspring.tool.modules.vivo.domain.SStores
import net.myspring.tool.modules.vivo.dto.SCustomerDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class FutureCustomerRepository @Autowired constructor(val namedParameterJdbcTemplate:NamedParameterJdbcTemplate){

    fun findVivoCustomers(date: LocalDate): MutableList<SCustomerDto> {
        val map = Maps.newHashMap<String, Any>()
        map.put("date",date)
        return namedParameterJdbcTemplate.query("""
            SELECT
                de.area_id AS customerId,
                de.NAME AS customerName,
                de.office_id AS zoneId,
                :date AS recordDate,
                1 AS customerLevel,
                '' AS customerStr1,
                de.area_id AS customerStr4
            FROM
                crm_depot de
            WHERE
                de.area_id IS NOT NULL
            OR de.area_id <> ''
            GROUP BY
                de.area_id
            UNION
                SELECT
                    de.id AS customerId,
                    de.NAME AS customerName,
                    de.area_id AS zoneId,
                    :date AS recordDate,
                    2 AS customerLevel,
                    de.area_type AS customerStr1,
                    de.area_id AS customerStr4
                FROM
                    crm_depot de
                WHERE
                    de.id IN (
                        SELECT
                            depot_id
                        FROM
                            crm_depot_shop
                        WHERE
                            depot_id NOT IN
                            (
                                SELECT
                                    depot_id
                                FROM
                                    crm_depot_store
                            )
                    )
        """,map, BeanPropertyRowMapper(SCustomerDto::class.java))
    }

    fun findIDVivoCustomers(date: LocalDate): MutableList<SCustomerDto> {
        val map = Maps.newHashMap<String, Any>()
        map.put("date",date)
        return namedParameterJdbcTemplate.query("""
            SELECT
                de.area_id AS customerId,
                de.NAME AS customerName,
                de.office_id AS zoneId,
                :date AS recordDate,
                1 AS customerLevel,
                '' AS customerStr1,
                de.area_id AS customerStr4
            FROM
                crm_depot de
            WHERE
                de.area_id IS NOT NULL
            OR de.area_id <> ''
            GROUP BY
                de.area_id
            UNION
                SELECT
                    de.id AS customerId,
                    de.NAME AS customerName,
                    de.office_id AS zoneId,
                    :date AS recordDate,
                    2 AS customerLevel,
                    de.area_type AS customerStr1,
                    de.area_id AS customerStr4
                FROM
                    crm_depot de
                WHERE
                    (de.area_id IS NOT NULL OR de.area_id <> '')
                    AND de.id IN (
                        SELECT
                            depot_id
                        FROM
                            crm_depot_shop
                        WHERE
                            depot_id NOT IN
                            (
                                SELECT
                                    depot_id
                                FROM
                                    crm_depot_store
                            )
                    )
        """,map, BeanPropertyRowMapper(SCustomerDto::class.java))
    }

    fun findOppoCustomers(): MutableList<CustomerDto> {
        val sb = StringBuffer()
        sb.append("""
       select
            de.area_id as areaId,
            de.office_id as officeId,
            de.id as depotId,
            de.name as depotName,
            de.depot_store_id as storeId,
            de.depot_shop_id as shopId,
            st.joint_level as jointLeavel,
            sh.sale_point_type as salePointType,
            de.mobile_phone as mobilePhone,
            sh.area_type as areaType,
            sh.bussiness_center_name as bussinessCenterName,
            sh.chain_type as chainType,
            sh.carrier_type as carrierType,
            sh.door_head as doorHead,
            sh.enable_date as enableDate,
            sh.channel_type as channelType,
            de.enabled as enabled,
            de.is_hidden as isHidden,
            de.district_id as districtId,
            sh.town_id as townId,
            sh.shop_area as shopArea,
            sh.frame_num as frameNum,
            sh.desk_double_num as deskDoubleNum,
            sh.desk_single_num as deskSingleNum,
            sh.cabinet_num as cabinetNum
        from crm_depot de
            left join crm_depot_store st on de.depot_store_id = st.id
            left join crm_depot_shop sh on de.depot_shop_id = sh.id
            order by de.id asc
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertyRowMapper(CustomerDto::class.java))
    }

    fun findIDvivoStore():MutableList<SStores>{
        val sb = StringBuilder()
        sb.append("""
          select
             de.province_id as storeID,'' as storeName,'' as agentCode,store.joint_level as jointLevel
        from
            crm_depot de,
            crm_depot_store store
        where
            de.id=store.depot_id
            and (store.joint_level='一级' or store.joint_level='二级')
            group by de.province_id
            order by store.joint_level asc
        """)
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertyRowMapper(SStores::class.java))
    }

    fun findImooCustomer():MutableList<SCustomerDto>{
        return namedParameterJdbcTemplate.query("""
            SELECT
                de.id as customerId,
                de.name as customerName,
                "M13A00" as agentCode
            FROM
                crm_depot de
            LEFT JOIN crm_depot_store st ON de.depot_store_id = st.id
            WHERE st.joint_level = '二级'
        """,BeanPropertyRowMapper(SCustomerDto::class.java))
    }
}