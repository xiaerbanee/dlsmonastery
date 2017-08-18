package net.myspring.report.modules.future.repository

import net.myspring.future.modules.basic.domain.ProductType
import net.myspring.report.modules.future.dto.ProductImeReportDto
import net.myspring.report.modules.future.dto.ProductTypeDto
import net.myspring.report.modules.future.web.query.ReportQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.lang.StringBuilder
import java.util.*

@Component
class ProductImeUploadRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

}