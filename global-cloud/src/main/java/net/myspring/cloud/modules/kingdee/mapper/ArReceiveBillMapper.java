package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

/**
 * 收款单
 * Created by lihx on 2017/5/12.
 */
@Mapper
public interface ArReceiveBillMapper {

    List<CustomerReceiveDto> findByPeriodForSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerIdList")HashSet<String> customerIdList);
}
