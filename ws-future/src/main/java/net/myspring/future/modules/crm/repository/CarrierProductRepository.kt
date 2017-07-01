package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSale
import net.myspring.future.modules.crm.domain.CarrierOrder
import net.myspring.future.modules.crm.domain.CarrierProduct
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


interface CarrierProductRepository : BaseRepository<CarrierProduct, String>, CarrierProductRepositoryCustom {
    fun findByName(name:String):CarrierProduct
}


interface CarrierProductRepositoryCustom {
}

class CarrierProductRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : CarrierProductRepositoryCustom {

}