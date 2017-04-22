package net.myspring.general.modules.sys.mapper;

import net.myspring.general.common.mybatis.MyMapper;
import net.myspring.general.modules.sys.domain.ProcessTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by liuj on 2017/4/22.
 */
@Mapper
public interface ProcessTaskMapper extends MyMapper<ProcessTask,String> {

}
