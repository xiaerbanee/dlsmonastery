package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.BankIn
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.dto.DemoPhoneDto
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import net.myspring.future.modules.crm.web.query.BankInQuery
import net.myspring.future.modules.crm.web.query.DemoPhoneQuery
import net.myspring.util.repository.QueryUtils
import net.myspring.util.text.StringUtils
import org.elasticsearch.common.collect.HppcMaps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityManager


interface BankInRepository : BaseRepository<BankIn, String>, BankInRepositoryCustom {

    fun findByShopIdAndType(shopId: String, type: String): BankIn

}

interface BankInRepositoryCustom{
    fun findPage(pageable: Pageable, bankInQuery : BankInQuery): Page<BankInDto>

    fun findDto(id: String): BankInDto
}

class BankInRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): BankInRepositoryCustom {
    override fun findDto(id: String): BankInDto {
        return namedParameterJdbcTemplate.queryForObject("""
        SELECT
            depot.name shopName,
            bank.name bankName,
            depot.client_id shopClientId,
            t1.*
        FROM
            crm_bank_in t1,
            crm_depot depot,
            crm_bank bank
        WHERE
            t1.enabled = 1
            AND t1.shop_id = depot.id
            AND depot.enabled = 1
            AND t1.bank_id = bank.id
            AND bank.enabled = 1
            AND t1.id = :id
          """, Collections.singletonMap("id", id), MyBeanPropertyRowMapper(BankInDto::class.java))
    }

    override fun findPage(pageable: Pageable, bankInQuery: BankInQuery): Page<BankInDto> {
        val sb = StringBuffer()
        sb.append("""
         SELECT
            depot.name shopName,
            bank.name bankName,
            depot.client_id shopClientId,
            t1.*
        FROM
            crm_bank_in t1,
            crm_depot depot,
            crm_bank bank
        WHERE
            t1.enabled = 1
            AND t1.shop_id = depot.id
            AND depot.enabled = 1
            AND t1.bank_id = bank.id
            AND bank.enabled = 1
        """)
        if(StringUtils.isNotBlank(bankInQuery.id)){
            sb.append("""  and t1.id=:id  """)
        }
        if(StringUtils.isNotBlank(bankInQuery.shopName)){
            sb.append("""  and depot.name like concat('%',:shopName, '%')  """)
        }
        if(bankInQuery.billDateStart != null){
            sb.append("""  and t1.bill_date >= :billDateStart  """)
        }
        if(bankInQuery.billDateEnd != null){
            sb.append("""  and t1.bill_date < :billDateEnd  """)
        }
        if(bankInQuery.amount!=null){
            sb.append("""  and t1.amount=:amount  """)
        }
        if(bankInQuery.inputDateStart!=null){
            sb.append("""  and t1.input_date >= :inputDateStart  """)
        }
        if(bankInQuery.inputDateEnd!=null){
            sb.append("""  and t1.input_date < :inputDateEnd  """)
        }
        if(bankInQuery.createdDateStart!=null){
            sb.append("""   and t1.created_date >= :createdDateStart """)
        }
        if(bankInQuery.createdDateEnd!=null){
            sb.append("""  and t1.created_date < :createdDateEnd  """)
        }
        if(StringUtils.isNotBlank(bankInQuery.outCode)){
            sb.append("""   and t1.out_code like concat('%',:outCode},'%'  """)
        }
        if(StringUtils.isNotBlank(bankInQuery.bankName)){
            sb.append("""  and bank.name like concat('%',:bankName,'%')  """)
        }
        if(StringUtils.isNotBlank(bankInQuery.processStatus)){
            sb.append("""  and t1.process_status =:processStatus  """)
        }
        if(StringUtils.isNotBlank(bankInQuery.createdBy)){
            sb.append("""  and t1.created_by =:createdBy  """)
        }
        if(StringUtils.isNotBlank(bankInQuery.serialNumber)){
            sb.append("""  and t1.serial_number like concat('%',:serialNumber,'%')  """)
        }


        val pageableSql = QueryUtils.getMySQLDialect().getPageableSql(sb.toString(), pageable)
        val countSql = QueryUtils.getMySQLDialect().getCountSql(sb.toString())
        val paramMap = QueryUtils.getParamMap(bankInQuery)

        return PageImpl<BankInDto>(namedParameterJdbcTemplate.query(pageableSql, paramMap, MyBeanPropertyRowMapper(BankInDto::class.java)), pageable,  namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java))



    }


}