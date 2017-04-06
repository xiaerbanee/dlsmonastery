package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.AccountChange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface AccountChangeMapper extends MyMapper<AccountChange,String> {

    Page<AccountChange> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

}
