package net.myspring.cloud.modules.report.repository

import net.myspring.cloud.modules.report.dto.SalProxyReceiveDto
import net.myspring.util.time.LocalDateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.HashMap

/**
 * 代理销售收款汇总报表
 */
@Component
class SalProxyReceiveRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findAll(dateStart: LocalDate, dateEnd: LocalDate): MutableList<SalProxyReceiveDto>? {
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", LocalDateUtils.format(dateStart))
        paramMap.put("dateEnd", LocalDateUtils.format(dateEnd))
        var sb = StringBuilder()
        sb.append("""
        select  case when a.FGROUPNUMBER is null then b.FGROUPNUMBER else a.FGROUPNUMBER end as groupNumber,
                case when a.FGROUPNAME is null then b.FGROUPNAME else a.FGROUPNAME end as groupName,
                case when a.FLX is null then b.FLX else a.FLX end as flx,
                a.FALLAMOUNT as fallAmount,
                b.FBANKNUMBER as bankNumber,
                b.FBANKNAME as bankName,
                b.FSKAMOUNT as skAmount,
                case when isnull(a.FALLAMOUNT,0)=0 then 0 else round(isnull(b.FSKAMOUNT,0)/a.FALLAMOUNT,4) end as backLV
        from (
        """)
        //销售出库单-销售退货（LX物料/TD物料）（其中对应的代理客户）总金额
        sb.append("""
            select b.FGROUPNUMBER,b.FGROUPNAME,a.FLX,sum(a.FALLAMOUNT) as FALLAMOUNT 
            from (
                select d.FLX,b.FCUSTOMERID as FCUSTID,c.FALLAMOUNT 
                from T_SAL_OUTSTOCKENTRY a 
                join T_SAL_OUTSTOCK b on a.FID=b.FID 
                join T_SAL_OUTSTOCKENTRY_F c on a.FID=c.FID and a.FENTRYID=c.FENTRYID 
                join (
                     select case when m1.fname like'%联信%' then 'LX' else 'TD' end as FLX,m1.FMATERIALID 
                     from T_BD_MATERIAL_L m1 
                     join T_BD_MATERIALBASE m2 on m1.FMATERIALID=m2.FMATERIALID and m1.FLOCALEID='2052' 
                     join T_BD_MATERIALCATEGORY m3 on m2.FCATEGORYID=m3.FCATEGORYID and m3.FNUMBER='CHLB10'
                ) d on a.FMATERIALID=d.FMATERIALID  
                where b.FDATE >= :dateStart and b.FDATE <= :dateEnd
                union all 
                select d.FLX,b.FRETCUSTID as FCUSTID,-c.FALLAMOUNT as FALLAMOUNT 
                from T_SAL_RETURNSTOCKENTRY a 
                join T_SAL_RETURNSTOCK b on a.FID=b.FID 
                join T_SAL_RETURNSTOCKENTRY_F c on a.FID=c.FID and a.FENTRYID=c.FENTRYID   
                join (
                    select case when m1.fname like'%联信%' then 'LX' else 'TD' end as FLX,m1.FMATERIALID 
                    from T_BD_MATERIAL_L m1 
                    join T_BD_MATERIALBASE m2 on m1.FMATERIALID=m2.FMATERIALID and m1.FLOCALEID='2052' 
                    join T_BD_MATERIALCATEGORY m3 on m2.FCATEGORYID=m3.FCATEGORYID and m3.FNUMBER='CHLB10'
                ) d on a.FMATERIALID=d.FMATERIALID  
                where b.FDATE >= :dateStart and b.FDATE <= :dateEnd
            ) a           
            join (
                select a.FCUSTID,c.FGROUPNUMBER,c.FGROUPNAME 
                from T_BD_CUSTOMER a 
                join (
                    select a.FID,a.FNUMBER as FGROUPNUMBER,b.FNAME as FGROUPNAME 
                    from T_BD_CUSTOMERGROUP a 
                    join T_BD_CUSTOMERGROUP_L b on a.FID=b.FID and b.FLOCALEID='2052' 
                    where b.FNAME like'%代理%'
                ) c on a.fprimarygroup=c.FID 
            ) b on a.FCUSTID=b.FCUSTID
            group by b.FGROUPNUMBER,b.FGROUPNAME,a.FLX
        """)
        sb.append( """  ) a """)
        sb.append( """
            FULL JOIN
            (
        """)
        //收款单-收款退款单（LX银行/TD银行）（其中对应的代理客户）总金额
        sb.append("""
            select b.FGROUPNUMBER,b.FGROUPNAME,c.FLX,c.FBANKNUMBER,c.FBANKNAME,SUM(a.FSKAMOUNT) as FSKAMOUNT
            from (
                select b.FCONTACTUNIT as FCUSTID,a.FACCOUNTID,a.FRECTOTALAMOUNTFOR as FSKAMOUNT
                from T_AR_RECEIVEBILLENTRY a join T_AR_RECEIVEBILL b on a.FID=b.FID
                where b.FDATE >= :dateStart and b.FDATE <= :dateEnd and a.FSETTLETYPEID in ('4')
                union all
                select b.FCONTACTUNIT as FCUSTID,a.FACCOUNTID,-a.FREFUNDAMOUNTFOR as FSKAMOUNT
                from T_AR_REFUNDBILLENTRY a join T_AR_REFUNDBILL b on a.FID=b.FID
                where b.FDATE >= :dateStart and b.FDATE <= :dateEnd and a.FSETTLETYPEID in ('4')
            ) a
            join (
                select a.FCUSTID,c.FGROUPNUMBER,c.FGROUPNAME
                from T_BD_CUSTOMER a
                join (
                    select a.FID,a.FNUMBER as FGROUPNUMBER,b.FNAME as FGROUPNAME
                    from T_BD_CUSTOMERGROUP a
                    join T_BD_CUSTOMERGROUP_L b on a.FID=b.FID and b.FLOCALEID='2052'
                    where b.FNAME like'%代理%'
                ) c on a.fprimarygroup=c.FID
            ) b on a.FCUSTID=b.FCUSTID
            join (
                select a.FBANKACNTID,case when b.FNAME like'LX-%' then 'LX' else 'TD' end as FLX,a.FNUMBER as FBANKNUMBER,b.FNAME as FBANKNAME
                from T_CN_BANKACNT a
                join T_CN_BANKACNT_L b on a.FBANKACNTID=b.FBANKACNTID and b.FLOCALEID='2052'
                where b.FNAME like 'TD-%' or b.FNAME like 'LX-%'
            ) c on a.FACCOUNTID=c.FBANKACNTID
            group by b.FGROUPNUMBER,b.FGROUPNAME,c.FLX,c.FBANKNUMBER,c.FBANKNAME
        """)
        sb.append( """ ) b on a.FGROUPNUMBER=b.FGROUPNUMBER and a.FLX=b.FLX """)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(SalProxyReceiveDto::class.java))
    }
}