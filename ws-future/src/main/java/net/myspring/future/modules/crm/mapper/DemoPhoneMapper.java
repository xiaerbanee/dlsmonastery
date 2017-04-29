package net.myspring.future.modules.crm.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.DemoPhone;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface DemoPhoneMapper extends MyMapper<DemoPhone,String> {

    Page<DemoPhone> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<DemoPhone> findByFilter(@Param("p") Map<String, Object> map);


}
