package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.common.repository.BaseRepository
import net.myspring.cloud.modules.sys.domain.Product
import net.myspring.cloud.modules.sys.dto.ProductDto
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

/**
 * Created by haos on 2017/5/24.
 */
interface ProductRepository : BaseRepository<Product,String>{
    @Query("""
        SELECT
        t1.name
        FROM  #{#entityName} t1
        WHERE
        t1.enabled=1
        AND  t1.companyName = :companyName
     """)
    fun findNameByCompanyName(@Param("companyName")companyName:String): MutableList<String>?

    @Query("""
        SELECT
        t1
        FROM  #{#entityName} t1
        WHERE
        t1.enabled=1
        AND  t1.companyName = :companyName
        and t1.name = :name
    """)
    fun findByNameAndCompanyName(@Param("companyName")companyName: String,@Param("name")name: String): Product

    @Query("""
        SELECT
        t1
        FROM  #{#entityName} t1
        WHERE
        t1.enabled=1
        AND  t1.companyName = :companyName
        and t1.code = :code
    """)
    fun findByCodeAndCompanyName(@Param("companyName")companyName: String,@Param("code")code: String): Product

    @Query("""
        SELECT
        t1
        FROM  #{#entityName} t1
        WHERE
        t1.enabled=1
        AND  t1.companyName = :companyName
    """)
    fun findByCompanyName(@Param("companyName")companyName: String): MutableList<Product>?

    @Query("""
        SELECT max(t1.outDate)
        FROM  #{#entityName} t1
        WHERE t1.companyName = :companyName
     """)
    fun findMaxOutDate(@Param("companyName")companyName:String): LocalDateTime

    @Query("""
        SELECT DISTINCT(t1.returnOutId)
        FROM  #{#entityName} t1
        WHERE  t1.companyName = :companyName
     """)
    fun findReturnOutId(@Param("companyName")companyName:String):String
}