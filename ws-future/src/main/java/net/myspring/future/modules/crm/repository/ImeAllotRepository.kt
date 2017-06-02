package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ImeAllot
import net.myspring.future.modules.crm.dto.ImeAllotDto
import net.myspring.future.modules.crm.dto.StoreAllotDto
import net.myspring.future.modules.crm.web.query.ImeAllotQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface ImeAllotRepository : BaseRepository<ImeAllot, String>,ImeAllotRepositoryCustom {

    fun findByProductImeId(productImeId: String): MutableList<ImeAllot>

}




interface ImeAllotRepositoryCustom{

    fun findPage(pageable: Pageable, imeAllotQuery: ImeAllotQuery): Page<ImeAllotDto>

    fun findDto(id: String): ImeAllotDto

}

class ImeAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ImeAllotRepositoryCustom{
    override fun findDto(id: String): ImeAllotDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findPage(pageable: Pageable, imeAllotQuery: ImeAllotQuery): Page<ImeAllotDto> {

        val sb = StringBuffer()
        sb.append("""
        SELECT
           t4.product_id productImeProductId,  t4.ime productImeIme, t1.*
        FROM
            crm_ime_allot t1,crm_depot t2,crm_depot t3, crm_product_ime t4
        WHERE
            t1.enabled=1
            and t1.from_depot_id = t2.id
            and t1.to_depot_id = t3.id
            and t1.product_ime_id = t4.id
            and t4.enabled = 1
        """)
        if(StringUtils.isNotBlank(imeAllotQuery.ime)){
            sb.append("""   and t4.ime like concat('%', :ime,'%')  """)
        }
        if(imeAllotQuery.crossArea !=null && imeAllotQuery.crossArea ){
            sb.append("""  AND t1.cross_area  = 1  """)
        }
        if(imeAllotQuery.crossArea !=null && !imeAllotQuery.crossArea ){
            sb.append("""  AND t1.cross_area  = 0  """)
        }

        if(StringUtils.isNotBlank(imeAllotQuery.fromDepotName)){
            sb.append("""   AND t2.name LIKE  CONCAT('%', :fromDepotName,'%')  """)
        }
        if(StringUtils.isNotBlank(imeAllotQuery.toDepotName)){
            sb.append("""  AND t3.name  LIKE CONCAT('%', :toDepotName,'%') """)
        }
        if(StringUtils.isNotBlank(imeAllotQuery.status)){
            sb.append("""  AND t1.status  = :status  """)
        }

        if(imeAllotQuery.createdDateStart != null){
            sb.append("""  AND t1.created_date >= :createdDateStart """)
        }
        if(imeAllotQuery.createdDateEnd != null){
            sb.append("""  AND t1.created_date < :createdDateEnd """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val paramMap = BeanPropertySqlParameterSource(imeAllotQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ImeAllotDto::class.java))

        return PageImpl(list,pageable,((pageable.pageNumber + 100) * pageable.pageSize).toLong())

    }


}