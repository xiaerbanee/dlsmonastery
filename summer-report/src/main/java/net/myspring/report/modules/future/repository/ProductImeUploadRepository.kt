package net.myspring.report.modules.future.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class ProductImeUploadRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

}