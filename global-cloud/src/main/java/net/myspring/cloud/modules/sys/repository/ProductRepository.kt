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
        AND  t1.companyId = :companyId
     """)
    fun findNameByCompanyId(@Param("companyId")companyId:String): MutableList<String>

    @Query("""
        SELECT
        t1
        FROM  #{#entityName} t1
        WHERE
        t1.enabled=1
        AND  t1.companyId = :companyId
        and t1.name = :name
    """)
    fun findByNameAndCompanyId(@Param("companyId")companyId: String,@Param("name")name: String): Product?

    @Query("""
        SELECT
        t1
        FROM  #{#entityName} t1
        WHERE
        t1.enabled=1
        AND  t1.companyId = :companyId
        and t1.code = :code
    """)
    fun findByCodeAndCompanyId(@Param("companyId")companyId: String,@Param("code")code: String): Product?

    @Query("""
        SELECT
        t1
        FROM  #{#entityName} t1
        WHERE
        t1.enabled=1
        AND  t1.companyId = :companyId
    """)
    fun findByCompanyId(@Param("companyId")companyId: String): MutableList<Product>

    @Query("""
        SELECT max(t1.outDate)
        FROM  #{#entityName} t1
        WHERE t1.companyId = :companyId
     """)
    fun findMaxOutDate(@Param("companyId")companyId:String): LocalDateTime

    @Query("""
        SELECT DISTINCT(t1.returnOutId)
        FROM  #{#entityName} t1
        WHERE  t1.companyId = :companyId
     """)
    fun findReturnOutId(@Param("companyId")companyId:String):String
}