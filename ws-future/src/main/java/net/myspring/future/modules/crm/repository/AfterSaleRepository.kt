package net.myspring.future.modules.crm.repository

import net.myspring.future.common.enums.AfterSaleTypeEnum
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSale
import net.myspring.future.modules.crm.dto.AfterSaleDto
import net.myspring.future.modules.crm.dto.GoodsOrderDto
import net.myspring.future.modules.crm.web.query.AfterSaleQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.util.*


interface AfterSaleRepository : BaseRepository<AfterSale, String>,AfterSaleRepositoryCustom {

    @Query("""
    SELECT t1
    FROM #{#entityName} t1, ProductIme t2
    WHERE t1.badProductImeId=t2.id
        AND t2.ime in ?1
        """)
    fun findByBadProductImeIn(imeList: MutableList<String>): MutableList<AfterSale>

    @Query("""
    SELECT MAX(t1.businessId)
    FROM #{#entityName} t1
    WHERE t1.createdDate >= ?1
        """)
    fun findMaxBusinessId(dateStart: LocalDate): String

    fun findByBadProductImeIdIn(badProductImeId:MutableList<String>):MutableList<AfterSale>

}


interface AfterSaleRepositoryCustom{

    fun findFilter(afterSaleQuery : AfterSaleQuery):MutableList<AfterSaleDto>

    fun findPage(pageable: Pageable, afterSaleQuery : AfterSaleQuery): Page<AfterSaleDto>?

    fun findDtoByBadProductImeInAndType(imeList: MutableList<String>,type:String): MutableList<AfterSaleDto>

    fun findDtoByIds(ids: MutableList<String>): MutableList<AfterSaleDto>

}

class AfterSaleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AfterSaleRepositoryCustom {
    override fun findDtoByIds(ids: MutableList<String>): MutableList<AfterSaleDto> {
        val sb = StringBuilder()
        sb.append("""
             SELECT
                 t1.*,t2.ime as 'badProductIme',t3.name as 'badProductName',t4.name as 'badDepotName'
             FROM
                 crm_after_sale  t1 left join crm_product_ime t2 on t1.bad_product_ime_id=t2.id
                 left join crm_product t3 on t1.bad_product_id=t3.id
                 left join crm_depot t4 on t1.bad_depot_id=t4.id
                 left join crm_after_sale_flee t5 on t5.after_sale_id=t1.id
             WHERE
                 t1.enabled=1
                and t1.id in(:ids)
        """)
        var paramMap = HashMap<String, Any>()
        paramMap.put("ids", ids);
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(AfterSaleDto::class.java))
    }

    override fun findPage(pageable: Pageable, afterSaleQuery: AfterSaleQuery): Page<AfterSaleDto>? {
            val sb = StringBuilder()
            sb.append("""
             SELECT
                 t1.*,t8.ime,t8.flee_shop_name,t8.contact,t8.mobile_phone,t8.address,t8.buy_amount,t2.ime as 'badProductIme',t3.name as 'badProductName',
                 t4.name as 'badDepotName',t7.name as 'toDepotName',t6.name as 'fromDepotName',t5.remarks as 'detailRemarks',
                 t5.replace_amount,t5.input_date,t5.replace_date,t5.id as 'detailId'
             FROM
                crm_after_sale  t1 left join crm_product_ime t2 on t1.bad_product_ime_id=t2.id
                left join crm_product t3 on t1.bad_product_id=t3.id
                left join crm_depot t4 on t1.bad_depot_id=t4.id
                left join crm_after_sale_detail t5 on t5.after_sale_id=t1.id
                left join crm_depot t6 on t5.from_depot_id=t6.id
                left join crm_depot t7 on t5.to_depot_id=t7.id
                left join crm_after_sale_flee t8 on t8.after_sale_id=t1.id
                left join crm_product_ime t9 on t5.replace_product_ime_id=t9.id
             WHERE
                 t1.enabled=1
            """)
            if(CollectionUtil.isNotEmpty(afterSaleQuery.imeList)){
                sb.append(""" and( t2.ime in (:imeList)  or t8.ime in(:imeList))""")
            }
            if(afterSaleQuery.inputDateStart!=null){
                sb.append(""" and t5.input_date>=:inputDateStart""")
            }
            if(afterSaleQuery.inputDateEnd!=null){
                sb.append(""" and t5.input_date<=:inputDateEnd""")
            }
            if(StringUtils.isNotBlank(afterSaleQuery.type)){
                sb.append(""" and t5.type=:type""")
            }
            if(StringUtils.isNotBlank(afterSaleQuery.badDepotName)){
                sb.append(""" and t4.name LIKE CONCAT('%',:badDepotName,'%')""")
            }
            if(StringUtils.isNotBlank(afterSaleQuery.detailRemarks)){
                sb.append(""" and t5.remarks LIKE CONCAT('%',:detailRemarks,'%')""")
            }
            if(CollectionUtil.isNotEmpty(afterSaleQuery.replaceProductImeList)){
                sb.append(""" and t9.ime in(:replaceProductImeList)""")
            }
            var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
            var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
            var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(afterSaleQuery), BeanPropertyRowMapper(AfterSaleDto::class.java));
            var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(afterSaleQuery),Long::class.java);
            return PageImpl(list,pageable,count);
    }

    override fun findFilter(afterSaleQuery: AfterSaleQuery): MutableList<AfterSaleDto> {
        val sb = StringBuilder()
        sb.append("""
             SELECT
                 t1.*,t2.ime as 'badProductIme',t3.name as 'badProductName',t4.name as 'badDepotName',
                 t7.name as 'toDepotName',t6.name as 'fromDepotName',t5.remarks,t5.replace_amount,t8.ime
             FROM
                crm_after_sale  t1 left join crm_product_ime t2 on t1.bad_product_ime_id=t2.id
                left join crm_product t3 on t1.bad_product_id=t3.id
                left join crm_depot t4 on t1.bad_depot_id=t4.id
                left join crm_after_sale_flee t8 on t8.after_sale_id=t1.id,
                crm_after_sale_detail t5    left join crm_depot t6 on t5.from_depot_id=t6.id
                left join crm_depot t7 on t5.to_depot_id=t7.id
             WHERE
                 t1.enabled=1
                 and  t5.after_sale_id=t1.id
        """)
        if(CollectionUtil.isNotEmpty(afterSaleQuery.imeList)){
            sb.append(""" and t2.ime in (:imeList) """)
        }
        if(afterSaleQuery.inputDateStart!=null){
            sb.append(""" and t5.input_date>=:inputDateStart""")
        }
        if(afterSaleQuery.inputDateEnd!=null){
            sb.append(""" and t5.input_date<=:inputDateEnd""")
        }
        if(StringUtils.isNotBlank(afterSaleQuery.type)){
            sb.append(""" and t1.type=:type""")
        }
        if(StringUtils.isNotBlank(afterSaleQuery.inputType)){
            sb.append(""" and t5.type=:inputType""")
        }
        if(StringUtils.isNotBlank(afterSaleQuery.productTypeId)){
            sb.append(""" and t3.product_type_id=:productTypeId""")
        }
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(afterSaleQuery), BeanPropertyRowMapper(AfterSaleDto::class.java))
    }

    override fun findDtoByBadProductImeInAndType(imeList: MutableList<String>,type:String): MutableList<AfterSaleDto> {
        val sb = StringBuilder()
        sb.append("""
             SELECT
                 t1.*,t2.ime as 'badProductIme',t3.name as 'badProductName',t4.name as 'badDepotName',t5.ime,
                t5.flee_shop_name,t5.contact,t5.mobile_phone,t5.address,t5.buy_amount
             FROM
                 crm_after_sale  t1 left join crm_product_ime t2 on t1.bad_product_ime_id=t2.id
                 left join crm_product t3 on t1.bad_product_id=t3.id
                 left join crm_depot t4 on t1.bad_depot_id=t4.id
                 left join crm_after_sale_flee t5 on t5.after_sale_id=t1.id
             WHERE
                 t1.enabled=1
        """)
        if(AfterSaleTypeEnum.售后机.name.equals(type)){
            sb.append("AND t2.ime in (:imeList)")
        }else{
            sb.append("AND t5.ime in (:imeList)")
        }
        var paramMap = HashMap<String, Any>()
        paramMap.put("imeList", imeList);
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(AfterSaleDto::class.java))
    }
}