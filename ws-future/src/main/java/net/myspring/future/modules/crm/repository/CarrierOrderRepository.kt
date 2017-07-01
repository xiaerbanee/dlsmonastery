package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSale
import net.myspring.future.modules.crm.domain.CarrierOrder
import net.myspring.future.modules.crm.dto.CarrierOrderDto
import net.myspring.future.modules.crm.web.query.CarrierOrderQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface CarrierOrderRepository : BaseRepository<CarrierOrder, String>, CarrierOrderRepositoryCustom {
    fun findByCode(code:String):CarrierOrder

    fun findByGoodsOrderId(goodsOrderId:String):MutableList<CarrierOrder>

    fun findByCodeIn(codeList:MutableList<String>):MutableList<CarrierOrder>
}


interface CarrierOrderRepositoryCustom {
    fun findPage(pageable: Pageable, carrierOrderQuery: CarrierOrderQuery): Page<CarrierOrderDto>

    fun findFilter(carrierOrderQuery: CarrierOrderQuery): MutableList<CarrierOrderDto>
}

class CarrierOrderRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : CarrierOrderRepositoryCustom {
    override fun findFilter(carrierOrderQuery: CarrierOrderQuery): MutableList<CarrierOrderDto> {
        val sb = StringBuffer()
        sb.append("""
         SELECT
            t1.*,t3.business_id ,t2.area_id,t2.name as 'depotName',t3.ship_date,t4.name as 'carrierShopName',t3.status as 'goodsOrderStatus'
        FROM
            api_carrier_order t1 left join api_carrier_shop t4 on t1.carrier_shop_id=t4.id,
            crm_depot t2,crm_goods_order t3
        WHERE
            t1.enabled = 1
            and t1.goods_order_id=t3.id
            and t3.shop_id =t2.id
            AND t3.status in (:goodsOrderStatusList)
        """)
        if (StringUtils.isNotBlank(carrierOrderQuery.businessId)) {
            sb.append("""  and t3.business_id=:businessId  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.carrierOrderStatus)) {
            sb.append("""  and t1.status=:carrierOrderStatus  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.goodsOrderStatus)) {
            sb.append("""  and t3.status=:goodsOrderStatus  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.goodsOrderRemarks)) {
            sb.append("""  and t3.remarks=:goodsOrderRemarks  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.remarks)) {
            sb.append("""  and t1.remarks=:remarks  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.code)) {
            sb.append("""  and t1.code=:code  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.notEqualStatus)) {
            sb.append("""  and t1.status!=:notEqualStatus  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.notEqualStoreId)) {
            sb.append("""  and t3.store_id!=:notEqualStoreId  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.depotName)) {
            sb.append("""  and t2.name=:depotName  """)
        }
        if (carrierOrderQuery.createdDateStart!=null) {
            sb.append("""  and t1.created_date >=:createdDateStart  """)
        }
        if (carrierOrderQuery.createdDateEnd!=null) {
            sb.append("""  and t1.created_date <=:createdDateEnd  """)
        }
        if (carrierOrderQuery.shipDateStart!=null) {
            sb.append("""  and t3.ship_date >=:shipDateStart  """)
        }
        if (carrierOrderQuery.shipDateEnd!=null) {
            sb.append("""  and t1.ship_date <=:shipDateEnd  """)
        }
        if (CollectionUtil.isNotEmpty(carrierOrderQuery.depotIdList)) {
            sb.append("""  and t2.id in (:depotIdList)  """)
        }
        if (CollectionUtil.isNotEmpty(carrierOrderQuery.officeIdList)) {
            sb.append("""  and t2.office_id in (:officeIdList)  """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(carrierOrderQuery), BeanPropertyRowMapper(CarrierOrderDto::class.java));
    }

    override fun findPage(pageable: Pageable, carrierOrderQuery: CarrierOrderQuery): Page<CarrierOrderDto> {
        val sb = StringBuffer()
        sb.append("""
         SELECT
            t1.*,t3.business_id ,t2.area_id,t2.name as 'depotName',t3.ship_date,t4.name as 'carrierShopName',t3.status as 'goodsOrderStatus'
        FROM
            api_carrier_order t1 left join api_carrier_shop t4 on t1.carrier_shop_id=t4.id,
            crm_depot t2,crm_goods_order t3
        WHERE
            t1.enabled = 1
            and t1.goods_order_id=t3.id
            and t3.shop_id =t2.id
            AND t3.status in (:goodsOrderStatusList)
        """)
        if (StringUtils.isNotBlank(carrierOrderQuery.businessId)) {
            sb.append("""  and t3.business_id=:businessId  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.carrierOrderStatus)) {
            sb.append("""  and t1.status=:carrierOrderStatus  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.goodsOrderStatus)) {
            sb.append("""  and t3.status=:goodsOrderStatus  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.goodsOrderRemarks)) {
            sb.append("""  and t3.remarks=:goodsOrderRemarks  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.remarks)) {
            sb.append("""  and t1.remarks=:remarks  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.code)) {
            sb.append("""  and t1.code=:code  """)
        }
        if (StringUtils.isNotBlank(carrierOrderQuery.depotName)) {
            sb.append("""  and t2.name=:depotName  """)
        }
        if (carrierOrderQuery.createdDateStart!=null) {
            sb.append("""  and t1.created_date >=:createdDateStart  """)
        }
        if (carrierOrderQuery.createdDateEnd!=null) {
            sb.append("""  and t1.created_date <=:createdDateEnd  """)
        }
        if (carrierOrderQuery.shipDateStart!=null) {
            sb.append("""  and t3.ship_date >=:shipDateStart  """)
        }
        if (carrierOrderQuery.shipDateEnd!=null) {
            sb.append("""  and t1.ship_date <=:shipDateEnd  """)
        }
        if (CollectionUtil.isNotEmpty(carrierOrderQuery.depotIdList)) {
            sb.append("""  and t2.id in (:depotIdList)  """)
        }
        if (CollectionUtil.isNotEmpty(carrierOrderQuery.officeIdList)) {
            sb.append("""  and t2.office_id in (:officeIdList)  """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(), pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(carrierOrderQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql, paramMap, BeanPropertyRowMapper(CarrierOrderDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list, pageable, count)
    }
}