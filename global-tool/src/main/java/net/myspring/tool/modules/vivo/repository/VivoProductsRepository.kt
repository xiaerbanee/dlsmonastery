package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.modules.vivo.domain.VivoProducts;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by admin on 2016/10/17.
 */
interface VivoProductsRepository {

    fun findColorIds(colorIds: List<String>): List<String>

    fun batchSave(vivoProducts: List<VivoProducts>): Int

    fun findAll(): List<String>
}
