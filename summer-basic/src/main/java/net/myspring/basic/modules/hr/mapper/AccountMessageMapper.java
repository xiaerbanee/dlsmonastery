package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.AccountMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AccountMessageMapper extends MyMapper<AccountMessage,String> {

    List<AccountMessage> findByAccountId(@Param("accountId") String accountId, @Param("createdDateStart") LocalDateTime createdDateStart);

}
