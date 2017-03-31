package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.DutyAnnual;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DutyAnnualMapper extends MyMapper<DutyAnnual,String> {

}
