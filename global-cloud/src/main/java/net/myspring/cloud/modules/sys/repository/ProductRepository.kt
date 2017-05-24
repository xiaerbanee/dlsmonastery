package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.common.repository.BaseRepository
import net.myspring.cloud.modules.sys.domain.Product
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

/**
 * Created by haos on 2017/5/24.
 */
interface ProductRepository:BaseRepository<Product,String>{
    @Query("""
        SELECT
        t1.*
        FROM
        sys_product t1
        WHERE
        t1.enabled=1
        AND  t1.company_id = :companyId
     """, nativeQuery = true)
    fun findByCompanyId(@Param("companyId")companyId:String):List<Product>

    @Query("""
        SELECT max(t1.out_date)
        from sys_product t1
        WHERE t1.company_id = :companyId
     """, nativeQuery = true)
    fun findMaxOutDate(@Param("companyId")companyId:String): LocalDateTime

    @Query("""
        SELECT DISTINCT(t1.return_out_id)
        FROM sys_product t1
        WHERE  t1.company_id = :companyId
     """, nativeQuery = true)
    fun findReturnOutId(@Param("companyId")companyId:String):String
}