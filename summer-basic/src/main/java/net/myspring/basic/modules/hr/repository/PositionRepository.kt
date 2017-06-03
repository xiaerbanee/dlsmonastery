package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Position
import net.myspring.basic.modules.hr.dto.PositionDto
import net.myspring.basic.modules.hr.web.query.PositionQuery
import net.myspring.basic.modules.sys.dto.RoleDto
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
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

    @CachePut(key="#p0.id")
    fun save(position: Position): Position

}
interface PositionRepositoryCustom{
    fun findByNameLike(@Param("name") name: String): MutableList<Position>

    fun findPage(pageable: Pageable, positionQuery: PositionQuery): Page<PositionDto>
}
class PositionRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): PositionRepositoryCustom{
    override fun findPage(pageable: Pageable, positionQuery: PositionQuery): Page<PositionDto> {
        var sb = StringBuilder("select t1.* from hr_position t1 where t1.enabled=1 ");
        if(StringUtils.isNotBlank(positionQuery.name)) {
            sb.append(" and t1.name like concat('%',:name,'%')");
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(positionQuery), BeanPropertyRowMapper(PositionDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(positionQuery),Long::class.java);
        return PageImpl(list,pageable,count);
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