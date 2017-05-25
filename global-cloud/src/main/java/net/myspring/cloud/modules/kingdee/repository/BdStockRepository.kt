package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdStock
import org.springframework.data.jpa.repository.Query

/**
 * Created by haos on 2017/5/24.
 */
interface  BdStockRepository{
    @Query("""
        SELECT
            t1.FSTOCKID,
            t1.FNUMBER,
            t2.FNAME,
            t1.FGROUP,
            t4.FNAME AS groupName,
            t1.FMODIFYDATE
        FROM
            T_BD_STOCK t1,
            T_BD_STOCK_L t2,
            T_BD_STOCKGROUP t3,
            T_BD_STOCKGROUP_L t4
        WHERE
            t1.FSTOCKID = t2.FSTOCKID
            AND t1.FGROUP = t3.FID
            AND t3.FID = t4.FID
            and t2.FNAME like %?1%
     """, nativeQuery = true)
    fun findByNameLike(name:String):MutableList<BdStock>

    @Query("""
     SELECT
        t1.FSTOCKID,
        t1.FNUMBER,
        t2.FNAME,
        t1.FGROUP,
        t4.FNAME AS groupName,
        t1.FMODIFYDATE
     FROM
        T_BD_STOCK t1,
        T_BD_STOCK_L t2,
        T_BD_STOCKGROUP t3,
        T_BD_STOCKGROUP_L t4
      WHERE
        t1.FSTOCKID = t2.FSTOCKID
        AND t1.FGROUP = t3.FID
        AND t3.FID = t4.FID
        and t2.FNAME in ?1
     """, nativeQuery = true)
    fun findByNameList(nameList:MutableList<String>):MutableList<BdStock>

    @Query("""
    SELECT
        t1.FSTOCKID,
        t1.FNUMBER,
        t2.FNAME,
        t1.FGROUP,
        t4.FNAME AS groupName,
        t1.FMODIFYDATE
    FROM
        T_BD_STOCK t1,
        T_BD_STOCK_L t2,
        T_BD_STOCKGROUP t3,
        T_BD_STOCKGROUP_L t4
    WHERE
        t1.FSTOCKID = t2.FSTOCKID
        AND t1.FGROUP = t3.FID
        AND t3.FID = t4.FID
     """, nativeQuery = true)
    fun findAll():MutableList<BdStock>
}