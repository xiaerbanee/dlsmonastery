package net.myspring.future.modules.api.repository

import com.google.common.collect.Maps
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.api.domain.CarrierShop
import net.myspring.future.modules.api.dto.CarrierOrderDto
import net.myspring.future.modules.api.dto.CarrierShopDto
import net.myspring.future.modules.api.web.query.CarrierShopQuery
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
import java.util.*

/**
 * Created by wangzm on 2017/7/5.
 */
interface CarrierShopRepository : BaseRepository<CarrierShop, String>, CarrierShopRepositoryCustom {
    fun  findByName(name:String):CarrierShop

}


interface CarrierShopRepositoryCustom {
    fun findPage(pageable: Pageable, carrierShopQuery: CarrierShopQuery):Page<CarrierShopDto>

    fun  findByNameLikeAndTypeAndEnabledIsTrue(name:String,type:String):MutableList<CarrierShop>
}

class CarrierShopRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : CarrierShopRepositoryCustom {
    override fun findByNameLikeAndTypeAndEnabledIsTrue(name: String,type:String): MutableList<CarrierShop> {
        val sb = StringBuffer()
        sb.append("""
         SELECT
            t1.*
        FROM
            api_carrier_shop t1
        WHERE
            t1.enabled = 1
        """)
        if (StringUtils.isNotBlank(name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%')   """)
        }
        if (StringUtils.isNotBlank(type)) {
            sb.append("""  and t1.type =:type  """)
        }
        sb.append("""  limit 50  """)
        var paramMap= Maps.newHashMap<String,Any>()
        paramMap.put("name",name);
        paramMap.put("type",type);
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(CarrierShop::class.java))
    }

    override fun findPage(pageable: Pageable, carrierShopQuery: CarrierShopQuery):Page<CarrierShopDto> {
        val sb = StringBuffer()
        sb.append("""
         SELECT
            t1.*
        FROM
            api_carrier_shop t1
        WHERE
            t1.enabled = 1
        """)
        if (StringUtils.isNotBlank(carrierShopQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%')   """)
        }
        if (StringUtils.isNotBlank(carrierShopQuery.type)) {
            sb.append("""  and t1.type =:type   """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(), pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(carrierShopQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql, paramMap, BeanPropertyRowMapper(CarrierShopDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list, pageable, count)
    }

}