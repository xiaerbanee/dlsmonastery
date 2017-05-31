package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Position
import net.myspring.basic.modules.hr.dto.PositionDto
import net.myspring.basic.modules.hr.web.query.PositionQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/25.
 */
@CacheConfig(cacheNames = arrayOf("positions"))
interface PositionRepository : BaseRepository<Position,String>,PositionRepositoryCustom{

    @Cacheable
    override fun findOne(id: String): Position

    @CachePut(key="#id")
    fun save(position: Position): Position

}
interface PositionRepositoryCustom{
    fun findByNameLike(@Param("name") name: String): MutableList<Position>

    fun findPage(pageable: Pageable, positionQuery: PositionQuery): Page<PositionDto>
}
class PositionRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): PositionRepositoryCustom{
    override fun findPage(pageable: Pageable, positionQuery: PositionQuery): Page<PositionDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByNameLike(name: String): MutableList<Position> {
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
        return namedParameterJdbcTemplate.query(sb.toString(), Collections.singletonMap("name", name), BeanPropertyRowMapper(Position::class.java))
    }


}