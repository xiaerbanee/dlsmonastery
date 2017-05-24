package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdDepartment
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface  BdDepartmentRepository{
    @Query("""
        select
            t1.FDEPTID,
            t1.FNUMBER,
            t2.FFULLNAME as FNAME
        from
            T_BD_DEPARTMENT t1,
            T_BD_DEPARTMENT_L t2
        where
            t1.FDEPTID = t2.FDEPTID
        and t1.FDEPTID in ?1
     """, nativeQuery = true)
    fun findByIdList(idList:List<String>):List<BdDepartment>

    @Query("""
     select
        t1.FDEPTID,
        t1.FNUMBER,
        t2.FFULLNAME as FNAME
     from
        T_BD_DEPARTMENT t1,
        T_BD_DEPARTMENT_L t2
     where
        t1.FDEPTID = t2.FDEPTID
        and t2.FFULLNAME like %?1%
     """, nativeQuery = true)
    fun findByNameLike(name:List<BdDepartment>):List<BdDepartment>
}