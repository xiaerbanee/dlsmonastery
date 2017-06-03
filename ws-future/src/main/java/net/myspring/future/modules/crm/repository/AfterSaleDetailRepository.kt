package net.myspring.future.modules.crm.repository

import com.google.common.collect.Maps
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSaleDetail
import net.myspring.future.modules.crm.dto.AfterSaleDetailDto
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery
import org.elasticsearch.common.collect.HppcMaps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.HashMap


interface AfterSaleDetailRepository : BaseRepository<AfterSaleDetail, String>, AfterSaleDetailRepositoryCustom {


     fun findByAfterSaleIdInAndType(afterSaleIdList: MutableList<String>, type: String): MutableList<AfterSaleDetail>


}

interface AfterSaleDetailRepositoryCustom{
     fun findDtoByAfterSaleIdInAndType(afterSaleIdList: MutableList<String>, type: String): MutableList<AfterSaleDetailDto>
}

class AfterSaleDetailRepositoryCustomImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AfterSaleDetailRepositoryCustom {
     override fun findDtoByAfterSaleIdInAndType(afterSaleIdList: MutableList<String>, type: String): MutableList<AfterSaleDetailDto> {
          val sb = StringBuilder()
          sb.append("""
             SELECT
                 t1.*,t2.name as 'fromDepotId',t3.name as 'toDepotName',t4.ime as 'replaceProductIme',t5.name as 'replaceProductName'
             FROM
                 crm_after_sale_detail t1 left join crm_depot t2 on t1.from_depot_id=t2.id
                 left join crm_depot t3 on t1.to_depot_id=t3.id
                 left join crm_product_ime t4 on t1.replace_product_ime_id=t4.id
                 left join crm_product t5 on t1.replace_product_id=t5.id
             WHERE
                 t1.enabled=1
                and t1.type=:type
                and t1.after_sale_id in (:afterSaleIdList)
        """)
          var paramMap = HashMap<String, Any>()
          paramMap.put("type", type);
          paramMap.put("afterSaleIdList", afterSaleIdList);
          return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(AfterSaleDetailDto::class.java))
     }
}
