package net.myspring.future.modules.basic.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto;
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DemoPhoneTypeMapper extends MyMapper<DemoPhoneType,String> {

    List<DemoPhoneType> findAllByApplyEndDate(LocalDate applyEndDate);

    Page<DemoPhoneTypeDto> findPage(Pageable pageable, @Param("p")DemoPhoneTypeQuery demoPhoneTypeQuery);

    List<DemoPhoneType> findLabels(List<String> ids);

}
