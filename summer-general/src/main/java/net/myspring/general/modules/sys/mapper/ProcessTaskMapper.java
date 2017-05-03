package net.myspring.general.modules.sys.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.general.modules.sys.domain.ProcessTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by liuj on 2017/4/22.
 */
@Mapper
public interface ProcessTaskMapper extends MyMapper<ProcessTask,String> {

    ProcessTask findByNameAndExtendId(@Param("name")String name,@Param("extendId")String extendId);

    int setExtendId(@Param("processInstanceId")String processInstanceId,@Param("extendId")String extendId);
}
