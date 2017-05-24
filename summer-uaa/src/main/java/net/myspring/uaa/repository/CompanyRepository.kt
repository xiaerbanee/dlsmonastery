package net.myspring.uaa.repository

import org.springframework.data.jpa.repository.Query

interface CompanyRepository {
    @Query("""
      SELECT
        t1.name
        FROM
        sys_company t1
        WHERE
        t1.enabled=1
        and t1.id= ?1
        """, nativeQuery = true)
    fun findNameById(id: String): String

}
