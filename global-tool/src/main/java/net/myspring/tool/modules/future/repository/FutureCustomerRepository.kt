package net.myspring.tool.modules.future.repository

import net.myspring.tool.modules.future.dto.CustomerDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class FutureCustomerRepository @Autowired constructor(val namedParameterJdbcTemplate:NamedParameterJdbcTemplate){
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
}