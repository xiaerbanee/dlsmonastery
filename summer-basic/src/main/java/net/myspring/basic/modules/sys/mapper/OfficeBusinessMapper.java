package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.OfficeBusiness;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by wangzm on 2017/4/24.
 */
@Mapper
public interface OfficeBusinessMapper extends MyMapper<OfficeBusiness,String>{

    List<String> findBusinessIdById(String id);

    int removeByBusinessOfficeIds(List<String> businessOfficeids);
}
