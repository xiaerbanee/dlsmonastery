package net.myspring.cloud.modules.input.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lihx on 2017/4/6.
 */
@Mapper
public interface ArReceivableMapper {

    String findFBillNoByfSourceBillNo(@Param("fSourceBillNo")String fSourceBillNo);

}
