package net.myspring.future.modules.basic.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface DemoPhoneTypeMapper extends MyMapper<DemoPhoneType,String> {

    List<DemoPhoneType> findAllByApplyEndDate(LocalDate applyEndDate);

    Page<DemoPhoneType> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<DemoPhoneType> findLabels(List<String> ids);

}
