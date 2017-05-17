package net.myspring.cloud.modules.report.mapper;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveDetailQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/5/12.
 */
@Mapper
public interface CustomerReceiveMapper {

    //期末查询
    List<CustomerReceiveDto> findEndShouldGet(@Param("dateEnd") LocalDate dateEnd, @Param("customerIdList") List<String> customerIdList);

    List<CustomerReceiveDetailDto> findMainList(CustomerReceiveDetailQuery customerReceiveDetailQuery);

    List<CustomerReceiveDetailDto> findDetailList(CustomerReceiveDetailQuery customerReceiveDetailQuery);

}
