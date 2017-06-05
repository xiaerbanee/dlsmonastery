package net.myspring.general.modules.sys.repository

import net.myspring.basic.modules.sys.dto.OfficeDto
import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.FolderFile
import net.myspring.general.modules.sys.dto.FolderFileDto
import net.myspring.general.modules.sys.web.query.FolderFileQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

interface FolderFileRepository : BaseRepository<FolderFile, String>,FolderFileRepositoryCustom {

}

interface FolderFileRepositoryCustom {
    fun findPage(pageable: Pageable, folderFileQuery: FolderFileQuery): Page<FolderFileDto>;
}

class FolderFileRepositoryImpl@Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): FolderFileRepositoryCustom {
    override fun findPage(pageable: Pageable, folderFileQuery: FolderFileQuery): Page<FolderFileDto> {
        var sb = StringBuilder("select * from sys_folder_file where enabled=1 ");
        if(folderFileQuery.createdDateStart!=null) {
            sb.append(" and created_date>=:createdDateStart");
        }
        if(folderFileQuery.createdDateEnd!=null) {
            sb.append(" and created_date<:createdDateEnd");
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(folderFileQuery), BeanPropertyRowMapper(FolderFileDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(folderFileQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}