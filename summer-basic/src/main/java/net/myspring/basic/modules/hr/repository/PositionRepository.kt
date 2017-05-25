package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Position
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.query.Param
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/25.
 */
@CacheConfig(cacheNames = arrayOf("positions"))
interface PositionRepository : BaseRepository<Position,String>,PositionRepositoryCustom{

    @Cacheable
    override fun findOne(id: String): Position

    @CachePut(key="#id")
    fun save(position: Position): Int

}
interface PositionRepositoryCustom{
    fun findByNameLike(@Param("name") name: String): List<Position>
}
class PositionRepositoryImpl @Autowired constructor(val entityManager: EntityManager): PositionRepositoryCustom{
    override fun findByNameLike(name: String): List<Position> {
        var sb = StringBuilder()
        sb.append("""
            select
            t1.*
            from
            hr_position t1
            where
            t1.enabled=1
        """)
        if (StringUtils.isNotBlank(name)) {
            sb.append("""
                and (t1.name like %:name%)
            """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), Position::class.java)
        query.setParameter("name", name)
        return query.resultList as List<Position>
    }


}