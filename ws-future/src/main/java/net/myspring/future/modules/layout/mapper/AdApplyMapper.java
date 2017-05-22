package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.AdApply;
import net.myspring.future.modules.layout.dto.AdApplyDto;
import net.myspring.future.modules.layout.web.query.AdApplyQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface AdApplyMapper extends MyMapper<AdApply,String> {

    Page<AdApplyDto> findPage(Pageable pageable, @Param("p") AdApplyQuery adApplyQuery);

    List<AdApply> findByFilter(@Param("p") Map<String, Object> map);

    List<AdApplyDto> findByOutGroupIdAndDate(@Param("dateStart")LocalDate dateStart,@Param("outGroupIds")List<String> outGroupIds);

    List<String> findAllId();
}
