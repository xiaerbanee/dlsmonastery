package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.DemoPhoneTypeOffice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DemoPhoneTypeOfficeMapper extends MyMapper<DemoPhoneTypeOffice,String> {

    List<DemoPhoneTypeOffice> findByDemoPhoneTypeId(String demoPhoneTypeId);

}
