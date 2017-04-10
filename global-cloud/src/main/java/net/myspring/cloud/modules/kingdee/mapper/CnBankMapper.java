package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.kingdee.domain.CnBank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface CnBankMapper {
    List<CnBank> findAll(@Param("maxOutDate")LocalDateTime maxOutDate);
}
