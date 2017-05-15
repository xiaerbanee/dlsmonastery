package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 其他应收单
 * Created by lihx on 2017/5/12.
 */
@Mapper
public interface ArOtherRecableMapper {

    List<CustomerReceiveDto> findByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerIdList")Set<String> customerIdList);
}
