package net.myspring.future.modules.basic.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.DemoPhoneTypeOffice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DemoPhoneTypeOfficeMapper extends MyMapper<DemoPhoneTypeOffice,String> {

    List<DemoPhoneTypeOffice> findByDemoPhoneTypeId(String demoPhoneTypeId);

}
