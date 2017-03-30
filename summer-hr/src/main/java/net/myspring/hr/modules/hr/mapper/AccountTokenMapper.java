package net.myspring.hr.modules.hr.mapper;

import net.myspring.hr.common.mybatis.MyMapper;
import net.myspring.hr.modules.hr.domain.AccountToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountTokenMapper extends MyMapper<AccountToken,String> {

}
