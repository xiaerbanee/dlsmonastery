package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AdApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdApplyMapper extends MyMapper<AdApply,String> {

    Page<AdApply> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<AdApply> findByFilter(@Param("p") Map<String, Object> map);

    List<AdApply> findByOutGroupIdAndDate(@Param("p") Map<String, Object> map);

    List<String> findAllId();
}
