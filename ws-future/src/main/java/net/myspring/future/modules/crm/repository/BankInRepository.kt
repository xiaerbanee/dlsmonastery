package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.BankIn
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.web.query.BankInQuery
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


interface BankInRepository : BaseRepository<BankIn, String>, BankInRepositoryCustom {
}

interface BankInRepositoryCustom{

    fun findPage(pageable: Pageable, bankInQuery : BankInQuery): Page<BankInDto>

    fun findDto(id: String): BankInDto
}

class BankInRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): BankInRepositoryCustom {
    override fun findDto(id: String): BankInDto {
        return namedParameterJdbcTemplate.queryForObject("""
        SELECT
            depot.name shopName,
            bank.name bankName,
            depot.client_id shopClientId,
            t1.*
        FROM
            crm_bank_in t1
            LEFT JOIN crm_depot depot ON t1.shop_id = depot.id
            LEFT JOIN crm_bank bank ON t1.bank_id = bank.id
        WHERE
            t1.id = :id
          """, Collections.singletonMap("id", id), BeanPropertyRowMapper(BankInDto::class.java))
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
            crm_bank_in t1
            LEFT JOIN crm_depot depot ON t1.shop_id = depot.id
            LEFT JOIN crm_bank bank ON t1.bank_id = bank.id
        WHERE
            t1.enabled = 1
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
            sb.append("""  and t1.created_date >= :createdDateStart """)
        }
        if(bankInQuery.createdDateEnd!=null){
            sb.append("""  and t1.created_date < :createdDateEnd  """)
        }
        if(StringUtils.isNotBlank(bankInQuery.outCode)){
            sb.append("""  and t1.out_code like concat('%',:outCode,'%')  """)
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
        if(CollectionUtil.isNotEmpty(bankInQuery.depotIdList)){
            sb.append("""  and t1.shop_id in (:depotIdList)  """)
        }
        if(CollectionUtil.isNotEmpty(bankInQuery.officeIdList)){
            sb.append("""  and depot.office_id in (:officeIdList)  """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(bankInQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(BankInDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
