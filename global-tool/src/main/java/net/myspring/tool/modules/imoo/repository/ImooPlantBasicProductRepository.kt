package net.myspring.tool.modules.imoo.repository

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class ImooPlantBasicProductRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findSegment1s(segment1s: MutableList<String>): MutableList<String> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("segment1s", segment1s);
        return namedParameterJdbcTemplate.query("""
            select t.segment1
            from #{#entityName} t
            where t.segment1 in :segment1s
            """, paramMap, BeanPropertyRowMapper(String::class.java));
    }
}
