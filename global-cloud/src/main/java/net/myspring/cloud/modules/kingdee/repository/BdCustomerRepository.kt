package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdCustomer
import net.myspring.common.dto.NameValueDto
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */

interface BdCustomerRepository {
    @Query("""
   SELECT
        t1.FCUSTID,
        t1.FNUMBER,
        t1.FSALDEPTID,
        t2.FNAME,
        t1.FPRIMARYGROUP,
        t4.FNAME AS fprimaryGroupName,
        t1.FMODIFYDATE
      FROM
        T_BD_CUSTOMER t1,
        T_BD_CUSTOMER_L t2,
        T_BD_CUSTOMERGROUP t3,
        T_BD_CUSTOMERGROUP_L t4
      WHERE
        t1.FCUSTID = t2.FCUSTID
        AND t1.FPRIMARYGROUP = t3.FID
        AND t3.FID = t4.FID
     """, nativeQuery = true)
    fun findAll(): MutableList<BdCustomer>

    @Query("""
     SELECT
            t1.FCUSTID,
            t1.FNUMBER,
            t1.FSALDEPTID,
            t2.FNAME,
            t1.FPRIMARYGROUP,
            t4.FNAME AS fprimaryGroupName,
            t1.FMODIFYDATE
        FROM
            T_BD_CUSTOMER t1,
            T_BD_CUSTOMER_L t2,
            T_BD_CUSTOMERGROUP t3,
            T_BD_CUSTOMERGROUP_L t4
        WHERE
            t1.FCUSTID = t2.FCUSTID
            AND t1.FPRIMARYGROUP = t3.FID
            AND t3.FID = t4.FID
            and t2.FNAME in ?1
    """, nativeQuery = true)
    fun findByNameList(nameList: MutableList<String>): MutableList<BdCustomer>

    @Query("""
  SELECT
        t1.FCUSTID,
        t1.FNUMBER,
        t1.FSALDEPTID,
        t2.FNAME,
        t1.FPRIMARYGROUP,
        t4.FNAME AS fprimaryGroupName,
        t1.FMODIFYDATE
    FROM
        T_BD_CUSTOMER t1,
        T_BD_CUSTOMER_L t2,
        T_BD_CUSTOMERGROUP t3,
        T_BD_CUSTOMERGROUP_L t4
    WHERE
        t1.FCUSTID = t2.FCUSTID
        AND t1.FPRIMARYGROUP = t3.FID
        AND t3.FID = t4.FID
        and t1.FCUSTID in ?1
    """, nativeQuery = true)
    fun findByIdList(idList: MutableList<String>): MutableList<BdCustomer>

    @Query("""
    SELECT
        t1.FCUSTID,
        t1.FNUMBER,
        t1.FSALDEPTID,
        t2.FNAME,
        t1.FPRIMARYGROUP,
        t4.FNAME AS fprimaryGroupName,
        t1.FMODIFYDATE
    FROM
        T_BD_CUSTOMER t1,
        T_BD_CUSTOMER_L t2,
        T_BD_CUSTOMERGROUP t3,
        T_BD_CUSTOMERGROUP_L t4
    WHERE
        t1.FCUSTID = t2.FCUSTID
        AND t1.FPRIMARYGROUP = t3.FID
        AND t3.FID = t4.FID
        and t1.FCUSTID = :id
     """, nativeQuery = true)
    fun findById(@Param("id") id: String): BdCustomer

    @Query("""
     SELECT
        t1.FCUSTID,
        t1.FNUMBER,
        t1.FSALDEPTID,
        t2.FNAME,
        t1.FPRIMARYGROUP,
        t4.FNAME AS fprimaryGroupName,
        t1.FMODIFYDATE
     FROM
        T_BD_CUSTOMER t1,
        T_BD_CUSTOMER_L t2,
        T_BD_CUSTOMERGROUP t3,
        T_BD_CUSTOMERGROUP_L t4
     where
        t1.FCUSTID = t2.FCUSTID
        AND t1.FPRIMARYGROUP = t3.FID
        AND t3.FID = t4.FID
        AND t2.FNAME like %?1%
     """, nativeQuery = true)
    fun findByNameLike(name: String): MutableList<BdCustomer>

    @Query("""
    SELECT
        t1.FPRIMARYGROUP as value,
        t4.FNAME AS name
    FROM
        T_BD_CUSTOMER t1,
        T_BD_CUSTOMER_L t2,
        T_BD_CUSTOMERGROUP t3,
        T_BD_CUSTOMERGROUP_L t4
    WHERE
        t1.FCUSTID = t2.FCUSTID
        AND t1.FPRIMARYGROUP = t3.FID
        AND t3.FID = t4.FID
        group by
        t1.FPRIMARYGROUP,
        t4.FNAME
     """, nativeQuery = true)
    fun findPrimaryGroupAndPrimaryGroupName():MutableList<NameValueDto>

}

