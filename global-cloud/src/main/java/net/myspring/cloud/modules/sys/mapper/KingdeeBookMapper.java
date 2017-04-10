package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface KingdeeBookMapper extends CrudMapper<KingdeeBook,String> {

    String findNameByCompanyId(@Param("companyId")String companyId);
}
