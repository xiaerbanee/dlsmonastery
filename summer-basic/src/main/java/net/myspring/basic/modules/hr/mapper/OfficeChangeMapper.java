package net.myspring.basic.modules.hr.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.OfficeChange;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OfficeChangeMapper extends MyMapper<OfficeChange,String> {

}
