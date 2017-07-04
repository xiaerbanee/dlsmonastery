package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.GlVoucher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * 凭证
 * Created by lihx on 2017/7/4.
 */
@Component
class GlVoucherRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate)  {
    fun findByBillNo(billNo:String):GlVoucher?{
        var list = namedParameterJdbcTemplate.query("""
            select *
            from T_GL_VOUCHER t1
            where t1.FBILLNO= :billNo
        """, Collections.singletonMap("billNo",billNo), BeanPropertyRowMapper(GlVoucher::class.java))
        if(list.size>0){
            return list[0]
        }else{
            return null
        }
    }
}