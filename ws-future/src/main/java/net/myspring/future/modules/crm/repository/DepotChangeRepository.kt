package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.*
import net.myspring.future.modules.crm.dto.DepotChangeDto

import net.myspring.future.modules.crm.web.query.DepotChangeQuery
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


interface DepotChangeRepository : BaseRepository<DepotChange, String>,DepotChangeRepositoryCustom {

}

interface DepotChangeRepositoryCustom{
    fun findFilter(depotChangeQuery: DepotChangeQuery):MutableList<DepotChangeDto>

    fun findPage(pageable: Pageable,depotChangeQuery: DepotChangeQuery):Page<DepotChangeDto>
}

class DepotChangeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):DepotChangeRepositoryCustom{

    override fun findFilter(depotChangeQuery: DepotChangeQuery):MutableList<DepotChangeDto>{
        val sb = StringBuffer()
        sb.append("""
         SELECT
            t1.*
        FROM
            crm_depot_change t1
        WHERE
            t1.enabled = 1
        """)
        if(StringUtils.isNotBlank(depotChangeQuery.depotId)){
            sb.append("""  and t1.depot_id=:depotId  """)
        }
        if(StringUtils.isNotBlank(depotChangeQuery.type)){
            sb.append("""  and t1.type = :type  """)
        }
        if(depotChangeQuery.createdDateStart != null){
            sb.append("""  and t1.expiry_date >= :createdDateStart  """)
        }
        if(depotChangeQuery.createdDateEnd != null){
            sb.append("""  and t1.expiry_date < :createdDateEnd  """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(depotChangeQuery), BeanPropertyRowMapper(DepotChangeDto::class.java))
    }

    override fun findPage(pageable: Pageable,depotChangeQuery: DepotChangeQuery):Page<DepotChangeDto>{
        val sb = StringBuffer()
        sb.append("""
         SELECT
            t1.*
        FROM
            crm_depot_change t1,
            crm_depot t2
        WHERE
            t1.enabled = 1
            and t1.depot_id = t2.id
        """)
        if(StringUtils.isNotBlank(depotChangeQuery.depotId)){
            sb.append("""  and t1.depot_id=:depotId  """)
        }
        if(StringUtils.isNotBlank(depotChangeQuery.type)){
            sb.append("""  and t1.type = :type  """)
        }
        if(depotChangeQuery.createdDateStart != null){
            sb.append("""  and t1.expiry_date >= :createdDateStart  """)
        }
        if(depotChangeQuery.createdDateEnd != null){
            sb.append("""  and t1.expiry_date < :createdDateEnd  """)
        }
        if (CollectionUtil.isNotEmpty(depotChangeQuery.officeIdList)) {
            sb.append("""  and t2.office_id in (:officeIdList)  """)
        }
        if (CollectionUtil.isNotEmpty(depotChangeQuery.depotIdList)) {
            sb.append("""  and t2.id in (:depotIdList)  """)
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(depotChangeQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(DepotChangeDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
