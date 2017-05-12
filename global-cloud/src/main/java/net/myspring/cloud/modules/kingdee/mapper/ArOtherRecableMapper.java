package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 其他出库单
 * Created by lihx on 2017/5/12.
 */
@Mapper
public interface ArOtherRecableMapper {

    List<CustomerReceiveDetailDto> findByPeriodForEntry();
}
