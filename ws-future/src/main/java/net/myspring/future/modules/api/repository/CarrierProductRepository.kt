package net.myspring.future.modules.api.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.api.domain.CarrierProduct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface CarrierProductRepository : BaseRepository<CarrierProduct, String>, CarrierProductRepositoryCustom {

    fun findByName(name:String): CarrierProduct

    fun findByProductIdIn(productIdList:MutableList<String>):MutableList<CarrierProduct>
}


interface CarrierProductRepositoryCustom {
}

class CarrierProductRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : CarrierProductRepositoryCustom {

}