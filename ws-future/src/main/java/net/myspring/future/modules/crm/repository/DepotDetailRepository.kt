package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.dto.DepotShopDto
import net.myspring.future.modules.basic.repository.DepotShopRepositoryCustom
import net.myspring.future.modules.basic.web.query.DepotQuery
import net.myspring.future.modules.crm.domain.DepotDetail

import net.myspring.future.modules.crm.dto.DepotDetailDto
import net.myspring.future.modules.crm.web.query.DepotDetailQuery
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


interface DepotDetailRepository : BaseRepository<DepotDetail, String>,DepotDetailRepositoryCustom {

}
interface DepotDetailRepositoryCustom{
    fun findPage(pageable: Pageable, depotDetailQuery: DepotDetailQuery): Page<DepotDetailDto>?
}


class DepotDetailRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DepotDetailRepositoryCustom {
    override fun findPage(pageable: Pageable, depotDetailQuery: DepotDetailQuery): Page<DepotDetailDto>? {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*,
                t2.name as 'productName',
                t3.name as 'depotName'
            FROM
                crm_depot_detail t1,
                crm_product t2,
                crm_depot t3
            WHERE
                t1.enabled = 1
            AND t2.enabled = 1
            AND t1.product_id = t2.id
            AND t1.depot_id = t3.id
        """)
        if (StringUtils.isNotEmpty(depotDetailQuery.shopName)) {
            sb.append("""  and t3.name LIKE CONCAT('%',:shopName,'%') """)
        }
        if (StringUtils.isNotEmpty(depotDetailQuery.productName)) {
            sb.append("""  and t2.name LIKE CONCAT('%',:productName,'%')  """)
        }
        if (depotDetailQuery.hasIme!=null){
            sb.append("""  and t1.has_ime=:hasIme """)
        }
        if (depotDetailQuery.isSame!=null) {
            sb.append("""  and t1.is_same=:isSame """)
        }
        if (CollectionUtil.isNotEmpty(depotDetailQuery.officeIdList)) {
            sb.append("""  and t3.office_id in (:officeIdList)  """)
        }
        if (CollectionUtil.isNotEmpty(depotDetailQuery.depotIdList)) {
            sb.append("""  and t3.id in (:depotIdList)  """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(), pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(depotDetailQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql, paramMap, BeanPropertyRowMapper(DepotDetailDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list, pageable, count)
    }
}
