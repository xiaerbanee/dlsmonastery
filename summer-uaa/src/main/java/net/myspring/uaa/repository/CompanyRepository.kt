package net.myspring.uaa.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class CompanyRepository @Autowired constructor(val entityManager: EntityManager) {
    fun findNameById(id: String): String {
        return entityManager.createNativeQuery("""
              SELECT
                t1.name
                FROM
                sys_company t1
                WHERE
                t1.enabled=1
                and t1.id= ?1
                """).setParameter("id",id).firstResult as String;
    }

}