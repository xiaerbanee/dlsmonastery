package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoProducts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

interface VivoProductsRepository : BaseRepository<VivoProducts, String>, VivoProductsRepositoryCustom {

}
interface VivoProductsRepositoryCustom{

}
class VivoProductsRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : VivoProductsRepositoryCustom{

}
