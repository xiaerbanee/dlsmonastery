package net.myspring.uaa.repository

import com.google.common.collect.Maps
import net.myspring.uaa.dto.OfficeBusinessDto
import net.myspring.uaa.dto.OfficeDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class OfficeRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findOne(id: String): OfficeDto {
        var sql = """
             SELECT
                t1.*
                FROM
                sys_office t1
                WHERE
                t1.enabled=1
                and t1.id= :id
        """;
        return namedParameterJdbcTemplate.queryForObject(sql, Collections.singletonMap("id",id), BeanPropertyRowMapper(OfficeDto::class.java))
    }

    fun findByParentIdsLike(parentId:String):MutableList<OfficeDto>{
        var sql = """
            SELECT t1.*
            FROM  sys_office t1
            where t1.parent_ids like :parentId
        """;
        return namedParameterJdbcTemplate.query(sql, Collections.singletonMap("parentId",parentId),BeanPropertyRowMapper(OfficeDto::class.java))
    }

    fun findByParentIdsListLike(parentIdList: MutableList<String>): MutableList<OfficeDto> {
        var sb = StringBuilder();
        sb.append("""
            SELECT t1.*
            FROM sys_office t1
            where  (
        """)
        for ((index) in parentIdList.withIndex()) {
            sb.append(" t1.parent_ids like :parentId").append(index);
            if (index < parentIdList.size - 1) {
                sb.append(" or ");
            }
        }
        sb.append(")");
        var paramMap = Maps.newHashMap<String, String>();
        for ((index, value) in parentIdList.withIndex()) {
            paramMap.put("parentId" + index, "%,$value,%");
        }
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(OfficeDto::class.java))
    }
}