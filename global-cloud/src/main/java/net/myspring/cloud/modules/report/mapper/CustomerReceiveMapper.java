package net.myspring.cloud.modules.report.mapper;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lihx on 2017/5/12.
 */
@Mapper
public interface CustomerReceiveMapper {

    //期末查询
    List<CustomerReceiveDto> findByEndDateAndCustomerIdList(@Param("dateEnd") LocalDate dateEnd, @Param("customerIdList") Set<String> customerIdList);

    List<CustomerReceiveDetailDto> findByPeriodForBillSum(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);

    List<CustomerReceiveDetailDto> findByPeriodForMaterial(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("customerId") String customerId);

}
